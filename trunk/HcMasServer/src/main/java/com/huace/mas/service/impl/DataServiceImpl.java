package com.huace.mas.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.huace.mas.bean.SpringProperty;
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
import com.huace.mas.service.DataService;

public class DataServiceImpl implements DataService {

	private final static Log log = LogFactory.getLog(DataServiceImpl.class);

	private DataDao dataDao;

	private SpringProperty springProperty;

	private long alertConfigLastModifiyTime = 0;

	private long alertConfigSize = 0;

	private Map<String, List<Kpi>> projectTypeMapping = new ConcurrentHashMap<String, List<Kpi>>();

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public List<Project> findAllProjects() {
		List<Project> projects = dataDao.findAllProjects();
		Map<String, List<Kpi>> pMap = this.checkAndRefresh();
		List<Project> result = new ArrayList<Project>();
		for (Project p : projects) {
			List<Kpi> kpis = pMap.get(p.getProjectName());
			if (kpis == null || kpis.size() == 0) {
				continue;
			}
			Set<String> ks = new HashSet<String>();
			for (Kpi k : kpis) {
				ks.add(k.getClass().getSimpleName());
			}
			p.setKpis(new ArrayList<String>(ks));
			result.add(p);
		}
		return result;
	}

	public void init() {
		this.checkAndRefresh();
	}

	@SuppressWarnings("unchecked")
	private synchronized Map<String, List<Kpi>> checkAndRefresh() {

		File file = new File(springProperty.getAlertConfigXmlPath());
		if (file.lastModified() != this.alertConfigLastModifiyTime
				|| file.length() != alertConfigSize) {
			Document doc = null;
			SAXReader reader = new SAXReader();
			try {
				doc = reader.read(file);
				List<Element> types = doc.getRootElement().elements("anyType");
				projectTypeMapping.clear();
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
					} else if (typeName.equals("Saturation")) {
						Saturation saturation = new Saturation();
						kpi = saturation;
						kpi.set__type("Saturation:#Shhc.Mass.ClassLibrary.entity");
						saturation.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Inner")) {
						Inner inner = new Inner();
						kpi = inner;
						kpi.set__type("Inner:#Shhc.Mass.ClassLibrary.entity");
						inner.setInit_x(getDouble(type.elementText("init_x")));
						inner.setYellow_x(getDouble(type
								.elementText("yellow_x")));
						inner.setOrange_x(getDouble(type
								.elementText("orange_x")));
						inner.setRed_x(getDouble(type.elementText("red_x")));

						inner.setInit_y(getDouble(type.elementText("init_y")));
						inner.setYellow_y(getDouble(type
								.elementText("yellow_y")));
						inner.setOrange_y(getDouble(type
								.elementText("orange_y")));
						inner.setRed_y(getDouble(type.elementText("red_y")));

						inner.setInit_y(getDouble(type.elementText("init_y")));
						inner.setYellow_y(getDouble(type
								.elementText("yellow_y")));
						inner.setOrange_y(getDouble(type
								.elementText("orange_y")));
						inner.setRed_y(getDouble(type.elementText("red_y")));
					} else if (typeName.equals("Rainfall")) {
						Rainfall rainfall = new Rainfall();
						kpi = rainfall;
						kpi.set__type("Rainfall:#Shhc.Mass.ClassLibrary.entity");
						rainfall.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("SeeFlow")) {
						SeeFlow seeFlow = new SeeFlow();
						kpi = seeFlow;
						kpi.set__type("SeeFlow:#Shhc.Mass.ClassLibrary.entity");
						seeFlow.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("DryBeach")) {
						DryBeach dryBeach = new DryBeach();
						kpi = dryBeach;
						kpi.set__type("DryBeach:#Shhc.Mass.ClassLibrary.entity");
						dryBeach.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Tyl")) {
						Tyl tyl = new Tyl();
						kpi = tyl;
						kpi.set__type("Tyl:#Shhc.Mass.ClassLibrary.entity");
						tyl.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Rxwy")) {
						Rxwy rxwy = new Rxwy();
						kpi = rxwy;
						kpi.set__type("Rxwy:#Shhc.Mass.ClassLibrary.entity");
						rxwy.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Lf")) {
						Lf lf = new Lf();
						kpi = lf;
						kpi.set__type("Lf:#Shhc.Mass.ClassLibrary.entity");
						lf.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Wd")) {
						Wd wd = new Wd();
						kpi = wd;
						kpi.set__type("Wd:#Shhc.Mass.ClassLibrary.entity");
						wd.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Thsl")) {
						Thsl thsl = new Thsl();
						kpi = thsl;
						kpi.set__type("Thsl:#Shhc.Mass.ClassLibrary.entity");
						thsl.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Dqwd")) {
						Dqwd dqwd = new Dqwd();
						kpi = dqwd;
						kpi.set__type("Dqwd:#Shhc.Mass.ClassLibrary.entity");
						dqwd.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Dqsd")) {
						Dqsd dqsd = new Dqsd();
						kpi = dqsd;
						kpi.set__type("Dqsd:#Shhc.Mass.ClassLibrary.entity");
						dqsd.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Dqyl")) {
						Dqyl dqyl = new Dqyl();
						kpi = dqyl;
						kpi.set__type("Dqyl:#Shhc.Mass.ClassLibrary.entity");
						dqyl.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Fs")) {
						Fs fs = new Fs();
						kpi = fs;
						kpi.set__type("Fs:#Shhc.Mass.ClassLibrary.entity");
						fs.setDamElevation(getDouble(type
								.elementText("damElevation")));
					} else if (typeName.equals("Fx")) {
						Fx fx = new Fx();
						kpi = fx;
						kpi.set__type("Fx:#Shhc.Mass.ClassLibrary.entity");
						fx.setDamElevation(getDouble(type
								.elementText("damElevation")));
					}
					kpi.setAlert(getBoolean(type.elementText("isAlert")));
					kpi.setDeviceID(getInteger(type.elementText("deviceID")));
					kpi.setPointName(type.elementText("pointName"));
					kpi.setProjectName(type.elementText("projectName"));
					if (!projectTypeMapping.containsKey(kpi.getProjectName())) {
						projectTypeMapping.put(kpi.getProjectName(),
								new ArrayList<Kpi>());
					}
					projectTypeMapping.get(kpi.getProjectName()).add(kpi);
				}
				this.alertConfigLastModifiyTime = file.lastModified();
				this.alertConfigSize = file.length();
			} catch (DocumentException e) {
				log.error("", e);
			}
		}
		return new HashMap<String, List<Kpi>>(this.projectTypeMapping);
	}

	private List<Kpi> cloneKpis(List<Kpi> kpis) {
		List<Kpi> clonedKpis = new ArrayList<Kpi>();
		try {
			for (Kpi kpi : kpis) {
				clonedKpis.add((Kpi) BeanUtils.cloneBean(kpi));
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return clonedKpis;
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

	private Integer getInteger(String s) {
		if (StringUtils.isEmpty(s)) {
			return -1;
		} else {
			return Integer.parseInt(s);
		}
	}

	@Override
	public List<String> getTypesForProject(Long projectID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<Object>> getKpisForProject(Long projectID) {
		Map<String, List<Kpi>> data = checkAndRefresh();
		Project project = dataDao.getProjectByID(projectID);
		if (project == null) {
			return new ArrayList<List<Object>>();
		}
		List<Kpi> kpis = data.get(project.getProjectName());
		List<Kpi> clonedKpis = this.cloneKpis(kpis);
		List<List<Object>> result = new ArrayList<List<Object>>();
		List<Object> projects = new ArrayList<Object>();
		projects.add(project);
		result.add(projects);
		for (int i = 0; i < springProperty.getOrderMap().size(); i++) {
			result.add(new ArrayList<Object>());
		}
		for (Kpi kpi : clonedKpis) {
			Kpi dbKpi = this.dataDao.queryDataByDeviceID(kpi.getDeviceID(),
					kpi.getClass());
			if (dbKpi == null) {
				continue;
			}
			int order = springProperty.getOrderMap().get(
					kpi.getClass().getSimpleName());
			kpi.setV1(dbKpi.getV1());
			kpi.setDacTime(dbKpi.getDacTime());
			if (kpi instanceof Surface) {
				((Surface) kpi).setV2(((Surface) dbKpi).getV2());
				((Surface) kpi).setV3(((Surface) dbKpi).getV3());
				Surface s = (Surface) kpi;
				s.setDis_x(s.getV1() - s.getInit_x());
				s.setDis_y(s.getV2() - s.getInit_y());
				s.setDis_h(s.getV3() - s.getInit_h());
				// 数据越大告警越大
				if (s.getDis_x() >= s.getRed_x()) {
					s.setAlertGrade_x(3);
				} else if (s.getDis_x() < s.getRed_x()
						&& kpi.getV1() >= s.getOrange_x()) {
					s.setAlertGrade_x(2);
				} else if (s.getDis_x() < s.getOrange_x()
						&& kpi.getV1() >= s.getYellow_x()) {
					s.setAlertGrade_x(1);
				} else {
					s.setAlertGrade_x(0);
				}

				if (s.getDis_y() >= s.getRed_y()) {
					s.setAlertGrade_y(3);
				} else if (s.getDis_y() < s.getRed_y()
						&& kpi.getV1() >= s.getOrange_y()) {
					s.setAlertGrade_y(2);
				} else if (s.getDis_y() < s.getOrange_y()
						&& kpi.getV1() >= s.getYellow_y()) {
					s.setAlertGrade_y(1);
				} else {
					s.setAlertGrade_y(0);
				}

				if (s.getDis_h() >= s.getRed_h()) {
					s.setAlertGrade_h(3);
				} else if (s.getDis_h() < s.getRed_h()
						&& kpi.getV1() >= s.getOrange_h()) {
					s.setAlertGrade_h(2);
				} else if (s.getDis_h() < s.getOrange_h()
						&& kpi.getV1() >= s.getYellow_h()) {
					s.setAlertGrade_h(1);
				} else {
					s.setAlertGrade_h(0);
				}

				s.setAlertGrade(s.getAlertGrade_x());
				if (s.getAlertGrade_y() > s.getAlertGrade()) {
					s.setAlertGrade(s.getAlertGrade_y());
				}
				if (s.getAlertGrade_h() > s.getAlertGrade()) {
					s.setAlertGrade(s.getAlertGrade_h());
				}
			} else if (kpi instanceof Inner) {
				((Inner) kpi).setV2(((Inner) dbKpi).getV2());

				Inner s = (Inner) kpi;
				s.setDis_x(s.getV1() - s.getInit_x());
				s.setDis_y(s.getV2() - s.getInit_y());
				// 数据越大告警越大
				if (s.getDis_x() >= s.getRed_x()) {
					s.setAlertGrade_x(3);
				} else if (s.getDis_x() < s.getRed_x()
						&& kpi.getV1() >= s.getOrange_x()) {
					s.setAlertGrade_x(2);
				} else if (s.getDis_x() < s.getOrange_x()
						&& kpi.getV1() >= s.getYellow_x()) {
					s.setAlertGrade_x(1);
				} else {
					s.setAlertGrade_x(0);
				}

				if (s.getDis_y() >= s.getRed_y()) {
					s.setAlertGrade_y(3);
				} else if (s.getDis_y() < s.getRed_y()
						&& kpi.getV1() >= s.getOrange_y()) {
					s.setAlertGrade_y(2);
				} else if (s.getDis_y() < s.getOrange_y()
						&& kpi.getV1() >= s.getYellow_y()) {
					s.setAlertGrade_y(1);
				} else {
					s.setAlertGrade_y(0);
				}

				s.setAlertGrade(s.getAlertGrade_x());
				if (s.getAlertGrade_y() > s.getAlertGrade()) {
					s.setAlertGrade(s.getAlertGrade_y());
				}
			} else if (kpi instanceof Saturation || kpi instanceof DryBeach) {
				// 数据越小告警越大
				if (kpi.getV1() <= kpi.getRed_value()) {
					kpi.setAlertGrade(3);
				} else if (kpi.getV1() > kpi.getRed_value()
						&& kpi.getV1() <= kpi.getOrange_value()) {
					kpi.setAlertGrade(2);
				} else if (kpi.getV1() > kpi.getOrange_value()
						&& kpi.getV1() <= kpi.getYellow_value()) {
					kpi.setAlertGrade(1);
				} else {
					kpi.setAlertGrade(0);
				}
			} else {
				// 数据越大告警越大
				if (kpi.getV1() >= kpi.getRed_value()) {
					kpi.setAlertGrade(3);
				} else if (kpi.getV1() < kpi.getRed_value()
						&& kpi.getV1() >= kpi.getOrange_value()) {
					kpi.setAlertGrade(2);
				} else if (kpi.getV1() < kpi.getOrange_value()
						&& kpi.getV1() >= kpi.getYellow_value()) {
					kpi.setAlertGrade(1);
				} else {
					kpi.setAlertGrade(0);
				}
			}
			result.get(order).add(kpi);
		}

		return result;
	}

}
