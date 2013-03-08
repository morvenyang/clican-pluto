package com.clican.appletv.core.service.config;

public interface ConfigService {

	public void saveConfig(String deviceId, String key, String value);
	
	public String getConfig(String deviceId, String key);
}
