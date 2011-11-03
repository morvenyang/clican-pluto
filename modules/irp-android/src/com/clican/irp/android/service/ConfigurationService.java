package com.clican.irp.android.service;

public interface ConfigurationService {

	public void configureHttpProxy(String proxyHost, Integer proxyPort,
			String userName, String password);
}
