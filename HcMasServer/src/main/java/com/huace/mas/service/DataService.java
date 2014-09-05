package com.huace.mas.service;

import java.util.List;

import com.huace.mas.entity.Project;

public interface DataService {

	public List<Project> findAllProjects();
	
	public List<String> getTypesForProject(Long projectID);
}
