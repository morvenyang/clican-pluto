package com.clican.irp.android.ui;

import com.clican.irp.android.R;

import android.os.Bundle;
import roboguice.activity.RoboActivity;

public class ConfigurationActivity extends RoboActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.configuration);
	}

}
