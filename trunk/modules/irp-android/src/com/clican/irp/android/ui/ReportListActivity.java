package com.clican.irp.android.ui;

import java.util.List;
import java.util.Map;

import roboguice.activity.RoboListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.clican.irp.android.R;
import com.clican.irp.android.enumeration.IntentName;
import com.clican.irp.android.enumeration.ReportScope;
import com.clican.irp.android.service.ReportService;
import com.clican.irp.android.util.IntentUtil;
import com.google.inject.Inject;

public class ReportListActivity extends RoboListActivity {

	private static final String[] REPORT_ATTRS = new String[] { "title",
			"author" };

	private static final int[] REPORT_IDS = new int[] { R.id.title, R.id.author };

	@Inject
	private ReportService reportService;

	private ReportScope reportScope;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		reportScope = IntentUtil.get(savedInstanceState, this.getIntent()
				.getExtras(), IntentName.REPORT_SCOPE);
		setContentView(R.layout.report_list);

		List<Map<String, Object>> list = reportService.queryReport(null,
				reportScope, null, null);
		setListAdapter(new SimpleAdapter(this, list, R.layout.report_row,
				REPORT_ATTRS, REPORT_IDS));

	}
}
