package com.clican.irp.android.ui;

import java.util.Map;

import roboguice.activity.RoboActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.clican.irp.android.R;
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
		this.setContentView(R.layout.report);
		Long reportId = IntentUtil.get(savedInstanceState, this.getIntent()
				.getExtras(), IntentName.REPORT_ID);
		Map<String, Object> report = reportService.readReport(reportId);
		TextView reportTitleView = (TextView) findViewById(R.id.report_title_view);
		reportTitleView.setText((String) report.get("title"));
		TextView reportSecondTitleView = (TextView) findViewById(R.id.report_second_title_view);
		reportSecondTitleView.setText((String) report.get("author"));
		TextView reportSummaryView = (TextView) findViewById(R.id.report_summary_view);
		reportSummaryView.setText((String) report.get("summary"),
				TextView.BufferType.SPANNABLE);
	}
}
