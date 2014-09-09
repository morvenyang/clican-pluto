package com.huace.mas.service;

import java.util.Date;
import java.util.List;

import com.huace.mas.bean.KpiData;
import com.huace.mas.entity.Project;

public interface DataService {

	public List<Project> findAllProjects();

	public List<String> getTypesForProject(Long projectID);

	public List<List<Object>> getKpisForProject(Long projectID);
	
	public List<KpiData> queryKpiData(Long projectID,String kpiType,String pointName,Date start,Date end);
}
