package com.huace.mas.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.huace.mas.dao.DataDao;
import com.huace.mas.entity.Dqsd;
import com.huace.mas.entity.Dqwd;
import com.huace.mas.entity.Dqyl;
import com.huace.mas.entity.DryBeach;
import com.huace.mas.entity.Fs;
import com.huace.mas.entity.Fx;
import com.huace.mas.entity.Inner;
import com.huace.mas.entity.Kpi;
import com.huace.mas.entity.Lf;
import com.huace.mas.entity.Project;
import com.huace.mas.entity.Rainfall;
import com.huace.mas.entity.Reservoir;
import com.huace.mas.entity.Rxwy;
import com.huace.mas.entity.Saturation;
import com.huace.mas.entity.SeeFlow;
import com.huace.mas.entity.Surface;
import com.huace.mas.entity.Thsl;
import com.huace.mas.entity.Tyl;
import com.huace.mas.entity.Wd;

public class DataDaoImpl extends JdbcDaoSupport implements DataDao {

	private Set<String> tables = new HashSet<String>();

	@SuppressWarnings("unchecked")
	public void init() {
		List<String> ts = this.getJdbcTemplate().queryForList(
				"SELECT name FROM sys.Tables", String.class);
		for (String t : ts) {
			tables.add(t.toUpperCase());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> findAllProjects() {
		List<Project> projects = this
				.getJdbcTemplate()
				.query("SELECT ProjectID as id,ProjectName FROM PR_Project where UseYn=1",
						new BeanPropertyRowMapper(Project.class));
		return projects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Project getProjectByID(Long projectID) {
		List<Project> projects = this
				.getJdbcTemplate()
				.query("SELECT ProjectID as id,ProjectName FROM PR_Project where UseYn=1 and ProjectID=?",
						new Object[] { projectID },
						new BeanPropertyRowMapper(Project.class));
		if (projects.size() > 0) {
			return projects.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Kpi queryDataByDeviceID(Integer deviceID, Class clazz) {
		String sql = "SELECT TOP 1 * FROM  ";
		String tableName = null;
		if (clazz.equals(Surface.class)) {
			tableName = "SURF_DeviceData";
		} else if (clazz.equals(Inner.class)) {
			tableName = "INMV_DeviceData";
		} else if (clazz.equals(Reservoir.class)) {
			tableName = "WATL_DeviceData";
		} else if (clazz.equals(Saturation.class)) {
			tableName = "PHRL_DeviceData";
		} else if (clazz.equals(Rainfall.class)) {
			tableName = "RAIN_DeviceData";
		} else if (clazz.equals(SeeFlow.class)) {
			tableName = "VAD_DeviceData";
		} else if (clazz.equals(DryBeach.class)) {
			tableName = "MBCH_DeviceData";
		} else if (clazz.equals(Tyl.class)) {
			tableName = "EARTHPRESSURE_DeviceData";
		} else if (clazz.equals(Rxwy.class)) {
			tableName = "RXWY_DeviceData";
		} else if (clazz.equals(Lf.class)) {
			tableName = "CREVICE_DeviceData";
		} else if (clazz.equals(Wd.class)) {
			tableName = "WD_DeviceData";
		} else if (clazz.equals(Thsl.class)) {
			tableName = "HYFROUS_DeviceData";
		} else if (clazz.equals(Dqwd.class)) {
			tableName = "ATEMPERATRURE_DeviceData";
		} else if (clazz.equals(Dqsd.class)) {
			tableName = "AHU_DeviceData";
		} else if (clazz.equals(Dqyl.class)) {
			tableName = "APRESS_DeviceData";
		} else if (clazz.equals(Fs.class)) {
			tableName = "WINDS_DeviceData";
		} else if (clazz.equals(Fx.class)) {
			tableName = "WINDD_DeviceData";
		}
		if (!tables.contains(tableName.toUpperCase())) {
			return null;
		}
		sql += tableName;
		if (clazz.equals(Inner.class)) {
			sql += " WHERE DeviceInnerID = ? order by DacTime desc";
		} else {
			sql += " WHERE DeviceID = ? order by DacTime desc";
		}

		List<Kpi> kpis = this.getJdbcTemplate().query(sql,
				new Object[] { deviceID }, new BeanPropertyRowMapper(clazz));
		if (kpis.size() > 0) {
			return kpis.get(0);
		} else {
			return null;
		}
	}

}
