package com.clican.irp.android.ui;

import java.text.SimpleDateFormat;
import java.util.Map;

import roboguice.activity.RoboActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.clican.irp.android.R;
import com.clican.irp.android.enumeration.IntentName;
import com.clican.irp.android.service.ReportService;
import com.clican.irp.android.util.IntentUtil;
import com.google.inject.Inject;

public class ReportActivity extends RoboActivity {

	private static final String[] REPORT_ATTRS = new String[] {
			"investRanking", "stockName", "industryName", "author", "date" };

	@Inject
	private ReportService reportService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.report);
		Long reportId = IntentUtil.get(savedInstanceState, this.getIntent()
				.getExtras(), IntentName.REPORT_ID);
		Map<String, Object> report = reportService.readReport(reportId);
		TextView reportTitleBar = (TextView) findViewById(R.id.report_title_bar);
		reportTitleBar.setText((String) report.get("title"));
		TextView reportTitleView = (TextView) findViewById(R.id.report_title_view);
		reportTitleView.setText((String) report.get("title"));
		TextView reportSecondTitleView = (TextView) findViewById(R.id.report_second_title_view);
		StringBuffer r = new StringBuffer();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM"
				+ this.getResources().getString(R.string.month) + "dd"
				+ this.getResources().getString(R.string.day));
		for (String key : REPORT_ATTRS) {
			Object value = report.get(key);
			if (value != null) {
				if (key.equals("date")) {
					try {
						r.append(sdf2.format(sdf1.parse((String) value)));
					} catch (Exception e) {

					}
				} else {
					r.append(value).append(" ");
				}

			}
		}
		reportSecondTitleView.setText(r.toString());
		TextView reportSummaryView = (TextView) findViewById(R.id.report_summary_view);
		reportSummaryView.setText(
				Html.fromHtml((String) report.get("summary")),
				TextView.BufferType.SPANNABLE);
	}
}
