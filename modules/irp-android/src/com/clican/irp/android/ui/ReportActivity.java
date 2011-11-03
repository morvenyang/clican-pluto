package com.clican.irp.android.ui;

import roboguice.activity.RoboActivity;
import android.os.Bundle;

import com.clican.irp.android.enumeration.IntentName;
import com.clican.irp.android.util.IntentUtil;

public class ReportActivity extends RoboActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Long reportId = IntentUtil.get(savedInstanceState, this.getIntent()
				.getExtras(), IntentName.REPORT_ID);
	}

}
