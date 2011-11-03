package com.clican.irp.android.ui;

import java.util.Map;

import roboguice.activity.RoboActivity;
import android.os.Bundle;

import com.clican.irp.android.enumeration.IntentName;
import com.clican.irp.android.service.ReportService;
import com.clican.irp.android.util.IntentUtil;
import com.google.inject.Inject;

public class ReportActivity extends RoboActivity {

	@Inject
	private ReportService reportService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Long reportId = IntentUtil.get(savedInstanceState, this.getIntent()
				.getExtras(), IntentName.REPORT_ID);
		Map<String, Object> report = reportService.readReport(reportId);
		
	}
}
