package com.clican.appletv.core.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.common.SpringProperty;

public class BaseClient {

	protected final Log log = LogFactory.getLog(getClass());

	protected Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

	protected Map<String, String> shortCacheMap = new ConcurrentHashMap<String, String>();

	protected Date lastExpireTime = DateUtils.truncate(new Date(),
			Calendar.DAY_OF_MONTH);

	protected Date lastShortExpireTime = new Date();

	protected SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	protected void checkCache() {
		Date current = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if (DateUtils.getFragmentInDays(current, Calendar.DAY_OF_MONTH) != DateUtils
				.getFragmentInDays(lastExpireTime, Calendar.DAY_OF_MONTH)) {
			if (log.isDebugEnabled()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				log.debug("clear cache current:" + sdf.format(current)
						+ ",lastExpireTime:" + sdf.format(lastExpireTime));
			}
			lastExpireTime = current;
			cacheMap.clear();
		}
		Date shortCurrent = new Date();
		shortCurrent = DateUtils.addMinutes(shortCurrent, -15);
		if (shortCurrent.after(lastShortExpireTime)) {
			lastShortExpireTime = new Date();
			cacheMap.clear();
		}
	}

	public PostResponse httpPost(String url, String content,
			Map<String, String> nameValuePairs, String reqContentType,
			String reqCharset, Map<String, String> headers, Integer timeout) {
		return this.httpPost(url, content, null, nameValuePairs,
				reqContentType, reqCharset, headers, timeout);
	}

	public PostResponse httpPost(String url, String content, byte[] payload,
			Map<String, String> nameValuePairs, String reqContentType,
			String reqCharset, Map<String, String> headers, Integer timeout) {

		InputStream is = null;
		ByteArrayOutputStream os1 = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream os2 = null;
		PostResponse pr = new PostResponse();
		try {
			HttpClient client = new HttpClient();
			if (springProperty.isSystemProxyEnable()) {
				client.getHostConfiguration().setProxy(
						springProperty.getSystemProxyHost(),
						springProperty.getSystemProxyPort());
			}

			PostMethod httpPost = new PostMethod(url);

			if (timeout != null) {
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(timeout);
				client.getHttpConnectionManager().getParams()
						.setSoTimeout(timeout);
			}
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpPost.addRequestHeader(key, headers.get(key));
				}
			}
			httpPost.addRequestHeader("Accept-Encoding", "gzip");
			if (payload != null) {
				httpPost.setRequestEntity(new ByteArrayRequestEntity(payload));
			} else if (StringUtils.isNotEmpty(content)) {
				httpPost.setRequestEntity(new StringRequestEntity(content,
						reqContentType, reqCharset));
			} else if (nameValuePairs != null) {
				NameValuePair[] pairs = new NameValuePair[nameValuePairs.size()];
				int i = 0;
				for (String key : nameValuePairs.keySet()) {
					pairs[i] = new NameValuePair(key, nameValuePairs.get(key));
					i++;
				}
				httpPost.setRequestBody(pairs);
			}

			int status = client.executeMethod(httpPost);
			pr.setStatus(status);

			if (log.isDebugEnabled()) {
				log.debug("Status:" + status + " for url:" + url);
			}

			for (Header header : httpPost.getResponseHeaders()) {
				if (header.getName().equals("Set-Cookie")) {
					try {
						String cookie = header.getValue().split(";")[0];
						int index = cookie.indexOf("=");
						String cookieName = cookie.substring(0, index);
						String cookieValue = cookie.substring(index + 1);
						pr.getCookieMap().put(cookieName, cookieValue);
					} catch (Exception e) {
						log.debug("Error to get cookie:" + header.getName()
								+ "=" + header.getValue());
					}
				}
			}
			if (status == 204) {
				return pr;
			}
			Header contentTypeHeader = httpPost
					.getResponseHeader("Content-Type");
			Header contentEncodingHeader = httpPost
					.getResponseHeader("Content-Encoding");
			String contentType = null;
			if (contentTypeHeader != null) {
				contentType = contentTypeHeader.getValue();
			}

			String charset = "UTF-8";
			String contentEncoding = null;
			if (StringUtils.isNotEmpty(contentType)) {
				int index = contentType.indexOf("charset=");
				if (index != -1) {
					charset = contentType.substring(index + 8).trim()
							.toLowerCase();
				}
				index = contentType.indexOf(";");
				if (index != -1) {
					contentType = contentType.substring(0, index).trim()
							.toLowerCase();
				}
			}
			if (contentEncodingHeader != null) {
				contentEncoding = contentEncodingHeader.getValue();
			}
			is = httpPost.getResponseBodyAsStream();
			os1 = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os1.write(buffer, 0, read);
			}
			if (StringUtils.isNotEmpty(contentEncoding)
					&& contentEncoding.equals("gzip")) {
				os2 = new ByteArrayOutputStream();
				gis = new GZIPInputStream(new ByteArrayInputStream(
						os1.toByteArray()));
				buffer = new byte[1024];
				read = -1;
				while ((read = gis.read(buffer)) != -1) {
					os2.write(buffer, 0, read);
				}
				String c = new String(os2.toByteArray(), charset);
				pr.setContent(c);
			} else {
				String c = new String(os1.toByteArray(), charset);
				pr.setContent(c);
			}
			return pr;
		} catch (Exception e) {
			if (e instanceof org.apache.commons.httpclient.ConnectTimeoutException
					|| e instanceof java.net.SocketException) {
				if (log.isDebugEnabled()) {
					log.debug("connection timeout for url:" + url);
				}
			} else {
				log.error("", e);
			}
			return pr;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (gis != null) {
				try {
					gis.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os1 != null) {
				try {
					os1.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os2 != null) {
				try {
					os2.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}

	}

	public String httpGet(String url) {
		return httpGet(url, null, null);
	}

	public PostResponse httpGetForCookie(String url,
			Map<String, String> headers, Integer timeout) {

		InputStream is = null;
		ByteArrayOutputStream os1 = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream os2 = null;
		PostResponse pr = new PostResponse();
		try {

			HttpClient client = new HttpClient();
			if (springProperty.isSystemProxyEnable()) {
				client.getHostConfiguration().setProxy(
						springProperty.getSystemProxyHost(),
						springProperty.getSystemProxyPort());
			}

			HttpMethod httpGet = new GetMethod(url);

			if (timeout != null) {
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(timeout);
				client.getHttpConnectionManager().getParams()
						.setSoTimeout(timeout);
			}
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpGet.addRequestHeader(key, headers.get(key));
				}
			}
			httpGet.addRequestHeader("Accept-Encoding", "gzip");
			int status = client.executeMethod(httpGet);
			pr.setStatus(status);
			if (log.isDebugEnabled()) {
				log.debug("Status:" + status + " for url:" + url);
			}
			for (Header header : httpGet.getResponseHeaders()) {
				if (header.getName().equals("Set-Cookie")) {
					try {
						String cookie = header.getValue().split(";")[0];
						String cookieName = cookie.split("=")[0];
						String cookieValue = cookie.split("=")[1];
						pr.getCookieMap().put(cookieName, cookieValue);
					} catch (Exception e) {
						log.debug("Error to get cookie:" + header.getName()
								+ "=" + header.getValue());
					}
				}
			}

			Header contentTypeHeader = httpGet
					.getResponseHeader("Content-Type");
			Header contentEncodingHeader = httpGet
					.getResponseHeader("Content-Encoding");
			String contentType = null;
			if(contentTypeHeader!=null){
				contentType = contentTypeHeader.getValue();
			}
			String charset = "UTF-8";
			String contentEncoding = null;
			if (StringUtils.isNotEmpty(contentType)) {
				int index = contentType.indexOf("charset=");
				if (index != -1) {
					charset = contentType.substring(index + 8).trim()
							.toLowerCase();
				}
				index = contentType.indexOf(";");
				if (index != -1) {
					contentType = contentType.substring(0, index).trim()
							.toLowerCase();
				}
			}
			if (contentEncodingHeader != null) {
				contentEncoding = contentEncodingHeader.getValue();
			}
			is = httpGet.getResponseBodyAsStream();
			os1 = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			long totalSize = 0;
			long mbsize = 0;
			long prembsize = 0;
			while ((read = is.read(buffer)) != -1) {
				os1.write(buffer, 0, read);
				totalSize = totalSize + read;
				mbsize = totalSize / (1024 * 1024);
				if (prembsize != mbsize) {
					prembsize = mbsize;
					log.debug("download size=" + prembsize + "MB");
				}
			}
			if (StringUtils.isNotEmpty(contentEncoding)
					&& contentEncoding.equals("gzip")) {
				os2 = new ByteArrayOutputStream();
				gis = new GZIPInputStream(new ByteArrayInputStream(
						os1.toByteArray()));
				buffer = new byte[1024];
				read = -1;
				while ((read = gis.read(buffer)) != -1) {
					os2.write(buffer, 0, read);
				}
				String c = new String(os2.toByteArray(), charset);
				pr.setContent(c);
			} else {
				String c = new String(os1.toByteArray(), charset);
				pr.setContent(c);
			}
			return pr;
		} catch (Exception e) {
			if (e instanceof org.apache.commons.httpclient.ConnectTimeoutException
					|| e instanceof java.net.SocketException) {
				if (log.isDebugEnabled()) {
					log.debug("connection timeout for url:" + url);
				}
			} else {
				log.error("", e);
			}
			return pr;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (gis != null) {
				try {
					gis.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os1 != null) {
				try {
					os1.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os2 != null) {
				try {
					os2.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}

	}

	public byte[] httpGetByData(String url, Map<String, String> headers,
			Integer timeout) {
		InputStream is = null;
		ByteArrayOutputStream os1 = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream os2 = null;
		try {
			HttpClient client = new HttpClient();
			if (springProperty.isSystemProxyEnable()) {
				client.getHostConfiguration().setProxy(
						springProperty.getSystemProxyHost(),
						springProperty.getSystemProxyPort());
			}

			HttpMethod httpGet = new GetMethod(url);

			if (timeout != null) {
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(timeout);
				client.getHttpConnectionManager().getParams()
						.setSoTimeout(timeout);
			}
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpGet.addRequestHeader(key, headers.get(key));
				}
			}
			httpGet.addRequestHeader("Accept-Encoding", "gzip");
			int status = client.executeMethod(httpGet);
			if (log.isDebugEnabled()) {
				log.debug("Status:" + status + " for url:" + url);
			}

			Header contentTypeHeader = httpGet
					.getResponseHeader("Content-Type");
			Header contentEncodingHeader = httpGet
					.getResponseHeader("Content-Encoding");
			String contentType = contentTypeHeader.getValue();
			String charset = "UTF-8";
			String contentEncoding = null;
			if (StringUtils.isNotEmpty(contentType)) {
				int index = contentType.indexOf("charset=");
				if (index != -1) {
					charset = contentType.substring(index + 8).trim()
							.toLowerCase();
				}
				index = contentType.indexOf(";");
				if (index != -1) {
					contentType = contentType.substring(0, index).trim()
							.toLowerCase();
				}
			}
			if (contentEncodingHeader != null) {
				contentEncoding = contentEncodingHeader.getValue();
			}
			is = httpGet.getResponseBodyAsStream();
			os1 = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os1.write(buffer, 0, read);
			}
			if (StringUtils.isNotEmpty(contentEncoding)
					&& contentEncoding.equals("gzip")) {
				os2 = new ByteArrayOutputStream();
				gis = new GZIPInputStream(new ByteArrayInputStream(
						os1.toByteArray()));
				buffer = new byte[1024];
				read = -1;
				while ((read = gis.read(buffer)) != -1) {
					os2.write(buffer, 0, read);
				}
				return os2.toByteArray();
			} else {
				return os1.toByteArray();
			}

		} catch (Exception e) {
			if (e instanceof org.apache.commons.httpclient.ConnectTimeoutException
					|| e instanceof java.net.SocketException) {
				if (log.isDebugEnabled()) {
					log.debug("connection timeout for url:" + url);
				}
			} else {
				log.error("", e);
			}
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (gis != null) {
				try {
					gis.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os1 != null) {
				try {
					os1.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os2 != null) {
				try {
					os2.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

	public String httpGet(String url, Map<String, String> headers,
			Integer timeout) {
		InputStream is = null;
		ByteArrayOutputStream os1 = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream os2 = null;
		try {
			HttpClient client = new HttpClient();
			if (springProperty.isSystemProxyEnable()) {
				client.getHostConfiguration().setProxy(
						springProperty.getSystemProxyHost(),
						springProperty.getSystemProxyPort());
			}

			HttpMethod httpGet = new GetMethod(url);

			if (timeout != null) {
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(timeout);
				client.getHttpConnectionManager().getParams()
						.setSoTimeout(timeout);
			}
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpGet.addRequestHeader(key, headers.get(key));
				}
			}
			httpGet.addRequestHeader("Accept-Encoding", "gzip");
			int status = client.executeMethod(httpGet);
			if (log.isDebugEnabled()) {
				log.debug("Status:" + status + " for url:" + url);
			}

			Header contentTypeHeader = httpGet
					.getResponseHeader("Content-Type");
			Header contentEncodingHeader = httpGet
					.getResponseHeader("Content-Encoding");
			String contentType = contentTypeHeader.getValue();
			String charset = "UTF-8";
			String contentEncoding = null;
			if (StringUtils.isNotEmpty(contentType)) {
				int index = contentType.indexOf("charset=");
				if (index != -1) {
					charset = contentType.substring(index + 8).trim()
							.toLowerCase();
				}
				index = contentType.indexOf(";");
				if (index != -1) {
					contentType = contentType.substring(0, index).trim()
							.toLowerCase();
				}
			}
			if (contentEncodingHeader != null) {
				contentEncoding = contentEncodingHeader.getValue();
			}
			is = httpGet.getResponseBodyAsStream();
			os1 = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os1.write(buffer, 0, read);
			}
			if (StringUtils.isNotEmpty(contentEncoding)
					&& contentEncoding.equals("gzip")) {
				os2 = new ByteArrayOutputStream();
				gis = new GZIPInputStream(new ByteArrayInputStream(
						os1.toByteArray()));
				buffer = new byte[1024];
				read = -1;
				while ((read = gis.read(buffer)) != -1) {
					os2.write(buffer, 0, read);
				}
				return new String(os2.toByteArray(), charset);
			} else {
				return new String(os1.toByteArray(), charset);
			}

		} catch (Exception e) {
			if (e instanceof org.apache.commons.httpclient.ConnectTimeoutException
					|| e instanceof java.net.SocketException) {
				if (log.isDebugEnabled()) {
					log.debug("connection timeout for url:" + url);
				}
			} else {
				log.error("", e);
			}
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (gis != null) {
				try {
					gis.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os1 != null) {
				try {
					os1.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
			if (os2 != null) {
				try {
					os2.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}
}
