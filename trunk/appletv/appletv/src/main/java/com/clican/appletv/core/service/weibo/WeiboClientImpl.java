package com.clican.appletv.core.service.weibo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class WeiboClientImpl implements WeiboClient {

	private final static Log log = LogFactory.getLog(WeiboClientImpl.class);

	@Override
	public void login(String username, String password) {
		if (log.isDebugEnabled()) {
			log.debug("fetch login page from sina weibo");
		}
		String url = "https://api.weibo.com/oauth2/authorize?client_id=2668371916&redirect_uri=http://10.0.1.5/appletv/weibo/oaAuthCallback.do";
		String responsePage = httpGet(url, null);
		if(log.isDebugEnabled()){
			log.debug(responsePage);
		}
	}

	private String httpGet(String url, Map<String, String> headers) {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			if (headers != null) {
				for (String key : headers.keySet()) {
					httpGet.addHeader(key, headers.get(key));
				}
			}
			HttpResponse response = client.execute(httpGet);
			if (log.isDebugEnabled()) {
				log.debug("Status:" + response.getStatusLine() + " for url:"
						+ url);
			}
			for (Header header : response.getAllHeaders()) {
				if (log.isDebugEnabled()) {
					log.debug(header.getName() + ":" + header.getValue());
				}
			}
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			os = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			return new String(os.toByteArray(), "utf-8");
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
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

	public static void main(String[] args) {
		WeiboClientImpl client = new WeiboClientImpl();
		client.login(null, null);
	}
}
