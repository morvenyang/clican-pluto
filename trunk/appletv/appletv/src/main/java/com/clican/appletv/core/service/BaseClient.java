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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class BaseClient {

	protected final Log log = LogFactory.getLog(getClass());

	protected Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

	protected Date lastExpireTime = DateUtils.truncate(new Date(),
			Calendar.DAY_OF_MONTH);

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
	}

	protected String httpGet(String url, Map<String, String> headers,
			Integer timeout) {
		InputStream is = null;
		ByteArrayOutputStream os1 = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream os2 = null;
		try {
			HttpClient client = new DefaultHttpClient();
			// client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
			// new HttpHost("web-proxy.corp.hp.com", 8080, "http"));
			HttpGet httpGet = new HttpGet(url);
			if (timeout != null) {
				HttpParams params = client.getParams();
				HttpConnectionParams.setConnectionTimeout(params, timeout);
				HttpConnectionParams.setSoTimeout(params, timeout);
			}
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}
			httpGet.addHeader("Accept-Encoding", "gzip");
			HttpResponse response = client.execute(httpGet);
			if (log.isDebugEnabled()) {
				log.debug("Status:" + response.getStatusLine() + " for url:"
						+ url);
			}

			HttpEntity entity = response.getEntity();
			Header contentTypeHeader = response.getFirstHeader("Content-Type");
			Header contentEncodingHeader = response
					.getFirstHeader("Content-Encoding");
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
			is = entity.getContent();
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
			log.error("", e);
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
