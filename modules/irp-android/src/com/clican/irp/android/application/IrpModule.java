package com.clican.irp.android.application;

import roboguice.application.RoboApplication;
import roboguice.config.AbstractAndroidModule;

import com.clican.irp.android.db.DatabaseHelper;
import com.clican.irp.android.db.PropertyDbAdapter;
import com.clican.irp.android.http.HttpGateway;
import com.clican.irp.android.http.impl.HttpGatewayImpl;
import com.clican.irp.android.service.LoginService;
import com.clican.irp.android.service.ReportService;
import com.clican.irp.android.service.impl.LoginServiceImpl;
import com.clican.irp.android.service.impl.ReportServiceImpl;

public class IrpModule extends AbstractAndroidModule {

	private RoboApplication application;

	public IrpModule(RoboApplication application) {
		this.application = application;
	}

	@Override
	protected void configure() {
		bind(LoginService.class).to(LoginServiceImpl.class);
		bind(ReportService.class).to(ReportServiceImpl.class);
		bind(HttpGateway.class).to(HttpGatewayImpl.class);
		bind(DatabaseHelper.class).toInstance(
				new DatabaseHelper(application.getBaseContext()));
		bind(PropertyDbAdapter.class);
	}

}
