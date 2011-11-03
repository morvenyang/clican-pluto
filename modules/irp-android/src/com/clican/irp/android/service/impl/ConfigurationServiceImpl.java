package com.clican.irp.android.service.impl;

import com.clican.irp.android.enumeration.PropertyName;
import com.clican.irp.android.service.ConfigurationService;
import com.clican.irp.android.service.PropertyService;
import com.google.inject.Inject;

public class ConfigurationServiceImpl implements ConfigurationService {

	@Inject
	private PropertyService propertyService;

	@Override
	public void configureHttpProxy(String proxyHost, Integer proxyPort,
			String userName, String password) {
		if (proxyHost != null && proxyHost.trim().length() > 0) {
			propertyService.createProperty(PropertyName.PROXY_HOST.name(),
					proxyHost.trim());
		} else {
			propertyService.deleteProperty(PropertyName.PROXY_HOST.name());
		}

		if (proxyPort != null) {
			propertyService.createProperty(PropertyName.PROXY_PORT.name(),
					proxyPort + "");
		} else {
			propertyService.deleteProperty(PropertyName.PROXY_PORT.name());
		}

		if (userName != null && userName.trim().length() > 0) {
			propertyService.createProperty(PropertyName.USER_NAME.name(),
					userName.trim());
		} else {
			propertyService.deleteProperty(PropertyName.USER_NAME.name());
		}

		if (password != null && password.trim().length() > 0) {
			propertyService.createProperty(PropertyName.PASSWORD.name(),
					password.trim());
		} else {
			propertyService.deleteProperty(PropertyName.PASSWORD.name());
		}
	}

}
