package com.huace.mas.service.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.huace.mas.bean.SpringProperty;
import com.huace.mas.dao.DataDao;
import com.huace.mas.entity.Kpi;
import com.huace.mas.entity.Project;
import com.huace.mas.entity.Reservoir;
import com.huace.mas.entity.Surface;
import com.huace.mas.service.DataService;

public class DataServiceImpl implements DataService {

	private final static Log log = LogFactory.getLog(DataServiceImpl.class);

	private DataDao dataDao;

	private SpringProperty springProperty;

	private long alertConfigLastModifiyTime = 0;

	private long alertConfigSize = 0;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public List<Project> findAllProjects() {
		return dataDao.findAllProjects();
	}

	public void init(){
		this.checkAndRefresh();
	}
	@SuppressWarnings("unchecked")
	private synchronized void checkAndRefresh() {
		SAXReader reader = new SAXReader();
		File file = new File(springProperty.getAlertConfigXmlPath());
		if (file.lastModified() != this.alertConfigLastModifiyTime
				|| file.length() != alertConfigSize) {
			Document doc = null;
			try {
				doc = reader.read(file);
				List<Element> types = doc.getRootElement().elements("anyType");
				for (Element type : types) {
					Kpi kpi = null;
					String typeName = type.elementText("typeName");
					if (typeName.equals("Surface")) {
						Surface surface = new Surface();
						kpi = surface;
						kpi.set__type("Surface:#Shhc.Mass.ClassLibrary.entity");
						surface.setInit_x(getDouble(type.elementText("init_x")));
						surface.setYellow_x(getDouble(type
								.elementText("yellow_x")));
						surface.setOrange_x(getDouble(type
								.elementText("orange_x")));
						surface.setRed_x(getDouble(type.elementText("red_x")));

						surface.setInit_y(getDouble(type.elementText("init_y")));
						surface.setYellow_y(getDouble(type
								.elementText("yellow_y")));
						surface.setOrange_y(getDouble(type
								.elementText("orange_y")));
						surface.setRed_y(getDouble(type.elementText("red_y")));

						surface.setInit_y(getDouble(type.elementText("init_y")));
						surface.setYellow_y(getDouble(type
								.elementText("yellow_y")));
						surface.setOrange_y(getDouble(type
								.elementText("orange_y")));
						surface.setRed_y(getDouble(type.elementText("red_y")));
					} else if (typeName.equals("Reservoir")) {
						Reservoir reservoir = new Reservoir();
						kpi = reservoir;
						kpi.set__type("Reservoir:#Shhc.Mass.ClassLibrary.entity");
						reservoir.setDamElevation(getDouble(type
								.elementText("damElevation")));
					}
					kpi.setAlert(getBoolean("isAlert"));
					kpi.setDeviceID(type.elementText("deviceID"));
					kpi.setPointName(type.elementText("pointName"));
					kpi.setProjectName(type.elementText("projectName"));
				}
			} catch (DocumentException e) {
				log.error("", e);
			}
		}
	}

	private Double getDouble(String s) {
		if (StringUtils.isEmpty(s)) {
			return 0.0;
		} else {
			return Double.parseDouble(s);
		}
	}

	private Boolean getBoolean(String s) {
		if (StringUtils.isEmpty(s)) {
			return false;
		} else {
			return Boolean.parseBoolean(s);
		}
	}

	@Override
	public List<String> getTypesForProject(Long projectID) {
		// TODO Auto-generated method stub
		return null;
	}

}
