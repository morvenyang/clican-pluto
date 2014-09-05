package com.huace.mas.dao.impl;

import java.util.ArrayList;
import java.util.List;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> findAllProjects() {
		List<Project> projects = this.getJdbcTemplate().query(
				"SELECT ProjectID as id,ProjectName FROM PR_Project where UseYn=1",
				new BeanPropertyRowMapper(Project.class));
		return projects;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Project getProjectByID(Long projectID) {
		List<Project> projects = this.getJdbcTemplate().query(
				"SELECT ProjectID as id,ProjectName FROM PR_Project where UseYn=1 and ProjectID=?",
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
		if (clazz.equals(Surface.class)) {
			sql += "SURF_DeviceData ";
		} else if (clazz.equals(Inner.class)) {
			sql += "INMV_DeviceData ";
		} else if (clazz.equals(Reservoir.class)) {
			sql += "WATL_DeviceData ";
		} else if (clazz.equals(Saturation.class)) {
			sql += "PHRL_DeviceData ";
		} else if (clazz.equals(Rainfall.class)) {
			sql += "RAIN_DeviceData ";
		} else if (clazz.equals(SeeFlow.class)) {
			sql += "VAD_DeviceData ";
		} else if (clazz.equals(DryBeach.class)) {
			sql += "MBCH_DeviceData ";
		} else if (clazz.equals(Tyl.class)) {
			sql += "EARTHPRESSURE_DeviceData ";
		} else if (clazz.equals(Rxwy.class)) {
			sql += "RXWY_DeviceData ";
		} else if (clazz.equals(Lf.class)) {
			sql += "CREVICE_DeviceData ";
		} else if (clazz.equals(Wd.class)) {
			sql += "WD_DeviceData ";
		} else if (clazz.equals(Thsl.class)) {
			sql += "HYFROUS_DeviceData ";
		} else if (clazz.equals(Dqwd.class)) {
			return null;
		} else if (clazz.equals(Dqsd.class)) {
			return null;
		} else if (clazz.equals(Dqyl.class)) {
			return null;
		} else if (clazz.equals(Fs.class)) {
			return null;
		} else if (clazz.equals(Fx.class)) {
			return null;
		}
		if (clazz.equals(Inner.class)) {
			sql += "WHERE DeviceInnerID = ? order by DacTime desc";
		}else{
			sql += "WHERE DeviceID = ? order by DacTime desc";
		}
		
		List<Kpi> kpis = this.getJdbcTemplate().query(sql,
				new Object[] { deviceID }, new BeanPropertyRowMapper(clazz));
		if(kpis.size()>0){
			return kpis.get(0);
		}else{
			return null;
		}
	}

}
