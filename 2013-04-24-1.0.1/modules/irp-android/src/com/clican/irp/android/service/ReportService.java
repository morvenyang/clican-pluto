package com.clican.irp.android.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.irp.android.enumeration.ReportScope;

public interface ReportService {

	public List<Map<String, Object>> queryReport(String query,
			ReportScope scope, Date start, Date end, int page, int pageSize);

	public Map<String, Object> readReport(Long reportId);


}
