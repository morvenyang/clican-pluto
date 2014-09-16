package com.huace.mas.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.huace.mas.bean.KpiData;
import com.huace.mas.entity.Kpi;
import com.huace.mas.entity.Project;

public interface DataService {

	public List<Project> findAllProjects();

	public List<List<Object>> getKpisForProject(Long projectID);
	
	public List<KpiData> queryKpiData(Long projectID,String kpiType,String pointName,Date start,Date end);
	
	public Map<String, List<Kpi>> checkAndRefresh();
}
