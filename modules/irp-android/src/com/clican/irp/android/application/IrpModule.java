package com.clican.irp.android.application;

import roboguice.config.AbstractAndroidModule;

import com.clican.irp.android.http.HttpGateway;
import com.clican.irp.android.http.impl.HttpGatewayMockImpl;
import com.clican.irp.android.service.LoginService;
import com.clican.irp.android.service.ReportService;
import com.clican.irp.android.service.impl.LoginServiceImpl;
import com.clican.irp.android.service.impl.ReportServiceImpl;

public class IrpModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(LoginService.class).to(LoginServiceImpl.class);
		bind(ReportService.class).to(ReportServiceImpl.class);
		bind(HttpGateway.class).to(HttpGatewayMockImpl.class);
	}

}
