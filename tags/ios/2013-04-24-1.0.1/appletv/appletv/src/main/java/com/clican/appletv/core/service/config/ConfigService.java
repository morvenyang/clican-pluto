package com.clican.appletv.core.service.config;

import java.util.Map;

public interface ConfigService {

	public void saveConfig(String deviceId, String key, String value);
	
	public String getConfig(String deviceId, String key);
	
	public Map<String,String> getAllConfig(String deviceId);
}
