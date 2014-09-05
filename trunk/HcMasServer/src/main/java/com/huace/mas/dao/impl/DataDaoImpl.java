package com.huace.mas.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.huace.mas.dao.DataDao;
import com.huace.mas.entity.Project;

public class DataDaoImpl extends JdbcDaoSupport implements DataDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> findAllProjects() {
		List<Project> projects = this.getJdbcTemplate().query(
				"SELECT * FROM PR_Project where UseYn=1", new BeanPropertyRowMapper(Project.class));
		return projects;
	}

}
