package com.clican.irp.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
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

	private static final String[] REPORT_ATTRS = new String[] {
			"reportTypeName", "title", "broker", "author", "investRanking",
			"stockName", "industryName" };

	@Inject
	private ReportService reportService;

	private ReportScope reportScope;

	private int page = 1;

	private int pageSize = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		reportScope = IntentUtil.get(savedInstanceState, this.getIntent()
				.getExtras(), IntentName.REPORT_SCOPE);
		setContentView(R.layout.report_list);

		List<Map<String, Object>> list = reportService.queryReport(null,
				reportScope, null, null, page, pageSize);
		List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> l : list) {
			StringBuffer r = new StringBuffer();
			for (String key : REPORT_ATTRS) {
				Object value = l.get(key);
				if (value != null) {
					r.append(value).append(" ");
				}
			}
			Map<String, Object> content = new HashMap<String, Object>();
			content.put("title", r.toString());
			contentList.add(content);
		}
		setListAdapter(new SimpleAdapter(this, contentList,
				R.layout.report_row, new String[] { "title" },
				new int[] { R.id.title }));

	}
}
