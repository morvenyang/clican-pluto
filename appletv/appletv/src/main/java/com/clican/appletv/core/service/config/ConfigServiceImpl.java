package com.clican.appletv.core.service.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.appletv.common.SpringProperty;

public class ConfigServiceImpl implements ConfigService {

	private final static Log log = LogFactory.getLog(ConfigServiceImpl.class);

	private Map<String, Map<String, String>> configMap = new ConcurrentHashMap<String, Map<String, String>>();

	private SpringProperty springProperty;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void destroy() {
		OutputStream os = null;
		try {
			String jsonContent = JSONObject.fromObject(configMap).toString();
			os = new FileOutputStream(springProperty.getUserConfigJsonFile());
			os.write(jsonContent.getBytes("utf-8"));
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

	public void init() {
		InputStream is = null;
		try {
			File file = new File(springProperty.getUserConfigJsonFile());
			if (!file.exists()) {
				return;
			}
			is = new FileInputStream(file);
			byte[] data = new byte[is.available()];
			is.read(data);
			String jsonContent = new String(data, "utf-8");
			if (StringUtils.isNotEmpty(jsonContent)) {
				JSONObject obj = JSONObject.fromObject(jsonContent);
				for (Object deviceId : obj.keySet()) {
					Map<String, String> configs = new ConcurrentHashMap<String, String>();
					configMap.put((String) deviceId, configs);
					JSONObject o = obj.getJSONObject((String) deviceId);
					for (Object key : o.keySet()) {
						configs.put((String) key, o.getString((String) key));
					}
				}
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

	@Override
	public void saveConfig(String deviceId, String key, String value) {
		if (configMap.containsKey(deviceId)) {
			configMap.put(deviceId, new ConcurrentHashMap<String, String>());
		}
		configMap.get(deviceId).put(key, value);
	}

	@Override
	public String getConfig(String deviceId, String key) {
		if (configMap.containsKey(deviceId)) {
			return configMap.get(deviceId).get(key);
		} else {
			return null;
		}
	}

}
