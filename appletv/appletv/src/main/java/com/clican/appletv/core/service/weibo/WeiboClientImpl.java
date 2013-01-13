package com.clican.appletv.core.service.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weibo4j.Account;
import weibo4j.org.json.JSONObject;

import com.clican.appletv.common.SpringProperty;

public class WeiboClientImpl implements WeiboClient {

	private final static Log log = LogFactory.getLog(WeiboClientImpl.class);

	private final static Map<String, String> deviceIdAccessTokenMap = new ConcurrentHashMap<String, String>();

	private SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public boolean saveUserInfo(String accessToken, String deviceId) {
		Account account = new Account();
		account.client.setToken(accessToken);
		try {
			JSONObject uid = account.getUid();
			if (StringUtils.isNotEmpty(uid.getString("uid"))) {
				deviceIdAccessTokenMap.put(deviceId, accessToken);
				return true;
			} else {
				log.debug("Get the uid failure, the user must rebind once again.");
				return false;
			}
		} catch (Exception e) {
			log.debug("Get the uid failure, the user must rebind once again.");
		}
		return false;
	}

	public void init() {
		InputStream is = null;
		try {
			File file = new File(springProperty.getWeiboTokenFile());
			if (!file.exists()) {
				return;
			}
			is = new FileInputStream(file);
			Properties props = new Properties();
			props.load(is);
			for (Entry<Object, Object> entry : props.entrySet()) {
				deviceIdAccessTokenMap.put((String) entry.getKey(),
						(String) entry.getValue());
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

	public void destory() {
		OutputStream os = null;
		try {
			File file = new File(springProperty.getWeiboTokenFile());
			if (file.exists()) {
				file.delete();
			}
			os = new FileOutputStream(springProperty.getWeiboTokenFile());
			for (String key : deviceIdAccessTokenMap.keySet()) {
				String entry = key + "=" + deviceIdAccessTokenMap.get(key);
				os.write(entry.getBytes("utf-8"));
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

	@Override
	public String getValidAccessToken(String deviceId) {
		String accessToken = deviceIdAccessTokenMap.get(deviceId);
		Account account = new Account();
		account.client.setToken(accessToken);
		try {
			JSONObject uid = account.getUid();
			if (StringUtils.isNotEmpty(uid.getString("uid"))) {
				return accessToken;
			} else {
				log.debug("Get the uid failure, the user must rebind once again.");
				return null;
			}
		} catch (Exception e) {
			log.debug("Get the uid failure, the user must rebind once again.");
		}

		return null;
	}
}
