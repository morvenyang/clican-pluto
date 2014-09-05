package com.huace.mas.dao;

import java.util.List;

import com.huace.mas.entity.Project;

public interface DataDao {
	
	public List<Project> findAllProjects();
	
	public Project getProjectByID(Long projectID);

}
