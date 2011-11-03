package com.clican.irp.android.service.impl;

import com.clican.irp.android.enumeration.PropertyName;
import com.clican.irp.android.model.HttpProxy;
import com.clican.irp.android.service.ConfigurationService;
import com.clican.irp.android.service.PropertyService;
import com.google.inject.Inject;

public class ConfigurationServiceImpl implements ConfigurationService {

	@Inject
	private PropertyService propertyService;

	private HttpProxy cachedHttpProxy;

	@Override
	public synchronized void configureHttpProxy(String proxyHost,
			Integer proxyPort, String proxyUserName, String proxyPassword) {
		cachedHttpProxy = new HttpProxy();
		if (proxyHost != null && proxyHost.trim().length() > 0) {
			propertyService.createProperty(PropertyName.PROXY_HOST.name(),
					proxyHost.trim());
			cachedHttpProxy.setProxyHost(proxyHost.trim());
		} else {
			propertyService.deleteProperty(PropertyName.PROXY_HOST.name());
		}

		if (proxyPort != null) {
			propertyService.createProperty(PropertyName.PROXY_PORT.name(),
					proxyPort + "");
			cachedHttpProxy.setProxyPort(proxyPort);
		} else {
			propertyService.deleteProperty(PropertyName.PROXY_PORT.name());
		}

		if (proxyUserName != null && proxyUserName.trim().length() > 0) {
			propertyService.createProperty(PropertyName.PROXY_USER_NAME.name(),
					proxyUserName.trim());
			cachedHttpProxy.setProxyUserName(proxyUserName.trim());
		} else {
			propertyService.deleteProperty(PropertyName.PROXY_USER_NAME.name());
		}

		if (proxyPassword != null && proxyPassword.trim().length() > 0) {
			propertyService.createProperty(PropertyName.PROXY_PASSWORD.name(),
					proxyPassword.trim());
			cachedHttpProxy.setProxyPassword(proxyUserName.trim());
		} else {
			propertyService.deleteProperty(PropertyName.PROXY_PASSWORD.name());
		}
	}

	@Override
	public synchronized HttpProxy getHttpProxy() {
		try {
			if (this.cachedHttpProxy == null) {
				cachedHttpProxy = new HttpProxy();
				cachedHttpProxy.setProxyHost(propertyService
						.getProperty(PropertyName.PROXY_HOST.name()));
				String proxyPortStr = propertyService
						.getProperty(PropertyName.PROXY_PORT.name());
				if (proxyPortStr != null && proxyPortStr.length() > 0) {
					cachedHttpProxy.setProxyPort(new Integer(proxyPortStr));
				}
				cachedHttpProxy.setProxyUserName(propertyService
						.getProperty(PropertyName.PROXY_USER_NAME.name()));
				cachedHttpProxy.setProxyPassword(propertyService
						.getProperty(PropertyName.PROXY_PASSWORD.name()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cachedHttpProxy != null && cachedHttpProxy.getProxyHost() != null
				&& cachedHttpProxy.getProxyHost().trim().length() > 0
				&& cachedHttpProxy.getProxyPort() > 0) {
			return cachedHttpProxy;
		}
		return null;
	}

}
