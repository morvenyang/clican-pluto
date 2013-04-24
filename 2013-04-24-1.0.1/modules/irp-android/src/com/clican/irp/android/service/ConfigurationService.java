package com.clican.irp.android.service;

import com.clican.irp.android.model.HttpProxy;

public interface ConfigurationService {

	public void configureHttpProxy(String proxyHost, Integer proxyPort,
			String proxyUserName, String proxyPassword);

	public HttpProxy getHttpProxy();
}
