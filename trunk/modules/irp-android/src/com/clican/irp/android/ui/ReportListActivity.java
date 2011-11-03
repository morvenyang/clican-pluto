package com.clican.irp.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.activity.RoboListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

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
			Map<String, Object> content = new HashMap<String, Object>();
			content.put("title", l);
			contentList.add(content);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, contentList,
				R.layout.report_row, new String[] { "title" },
				new int[] { R.id.title });
		adapter.setViewBinder(new ViewBinder() {
			@SuppressWarnings("unchecked")
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				final Map<String, Object> l = (Map<String, Object>) data;
				StringBuffer r = new StringBuffer();
				for (String key : REPORT_ATTRS) {
					Object value = l.get(key);
					if (value != null) {
						if (key.equals("reportTypeName")) {
							r.append("<font color='blue'>").append(value)
									.append("</font>").append(" ");
						} else {
							r.append(value).append(" ");
						}

					}
				}
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Long reportId = (Long) l.get("id");
						Intent reportIntent = new Intent(
								ReportListActivity.this, ReportActivity.class);
						reportIntent.putExtra(IntentName.REPORT_ID.name(),
								reportId);
						startActivity(reportIntent);
					}

				});
				Spanned html = Html.fromHtml(r.toString());
				((TextView) view).setText(html, TextView.BufferType.SPANNABLE);
				return true;
			}

		});
		setListAdapter(adapter);

	}
}
