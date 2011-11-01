package com.clican.irp.android.ui;

import roboguice.activity.RoboTabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.clican.irp.android.R;
import com.clican.irp.android.enumeration.IntentName;
import com.clican.irp.android.enumeration.ReportScope;

public class IrpAndroidActivity extends RoboTabActivity {

	private TabHost tabHost;

	@Override
	public void onContentChanged() {
		super.onContentChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		setupTabs();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void setupTabs() {
		tabHost = getTabHost();
		
		final String careReport = getResources().getText(R.string.care_report)
				.toString();
		final String innerReport = getResources()
				.getText(R.string.inner_report).toString();
		final String outerReport = getResources()
				.getText(R.string.outer_report).toString();

		Intent careReportIntent = new Intent(this, ReportListActivity.class);
		careReportIntent.putExtra(IntentName.REPORT_SCOPE.name(),
				ReportScope.CARE);
		Intent innerReportIntent = new Intent(this, ReportListActivity.class);
		innerReportIntent.putExtra(IntentName.REPORT_SCOPE.name(),
				ReportScope.INNER);
		Intent outerReportIntent = new Intent(this, ReportListActivity.class);
		outerReportIntent.putExtra(IntentName.REPORT_SCOPE.name(),
				ReportScope.OUTER);
		tabHost.addTab(tabHost.newTabSpec(careReport).setIndicator(careReport)
				.setContent(careReportIntent));
		tabHost.addTab(tabHost.newTabSpec(innerReport)
				.setIndicator(innerReport).setContent(innerReportIntent));
		tabHost.addTab(tabHost.newTabSpec(outerReport)
				.setIndicator(outerReport).setContent(outerReportIntent));

	}
}