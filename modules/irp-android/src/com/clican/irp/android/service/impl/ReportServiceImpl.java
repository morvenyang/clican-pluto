package com.clican.irp.android.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.clican.irp.android.enumeration.ReportScope;
import com.clican.irp.android.http.HttpGateway;
import com.clican.irp.android.http.impl.HttpGatewayImpl;
import com.clican.irp.android.service.ReportService;
import com.clican.irp.android.util.JSONUtil;
import com.google.inject.Singleton;

@Singleton
public class ReportServiceImpl implements ReportService {
	private HttpGateway httpGateway;

	public ReportServiceImpl() {
		httpGateway = new HttpGatewayImpl();
	}

	public List<Map<String, Object>> queryReport(String query,
			ReportScope scope, Date start, Date end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = "/apple/report/query.do?";
		if (query != null && query.trim().length() > 0) {
			url += "query=" + query + "&";
		}
		if (scope != null) {
			url += "scope=" + scope.getScope() + "&";
		}
		if (start != null) {
			url += "startDate=" + sdf.format(start) + "&";
		}
		if (end != null) {
			url += "startDate=" + sdf.format(end) + "&";
		}
		try {
			JSONObject result = httpGateway.invokeBySession(url);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			JSONArray array = result.getJSONArray("reports");
			for (int i = 0; i < array.length(); i++) {
				list.add(JSONUtil.convertJSON2Map(array.getJSONObject(i)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
