package com.clican.irp.android.application;

import java.util.List;

import roboguice.application.RoboApplication;

import com.google.inject.Module;

public class IrpApplication extends RoboApplication {

	protected void addApplicationModules(List<Module> modules) {
		modules.add(new IrpModule(this));
	}
}
