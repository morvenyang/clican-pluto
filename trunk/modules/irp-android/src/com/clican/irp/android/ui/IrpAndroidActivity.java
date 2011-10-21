package com.clican.irp.android.ui;

import roboguice.activity.RoboTabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.clican.irp.android.R;

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

		final String innerReport = getResources().getText(R.string.inner_report)
				.toString();
		final String outerReport = getResources().getText(R.string.outer_report)
				.toString();

		tabHost.addTab(tabHost.newTabSpec(innerReport)
				.setIndicator(innerReport)
				.setContent(new Intent(this, ReportListActivity.class)));
		tabHost.addTab(tabHost.newTabSpec(outerReport)
				.setIndicator(outerReport)
				.setContent(new Intent(this, ReportListActivity.class)));
		
	}
}