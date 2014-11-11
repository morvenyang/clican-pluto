package com.huace.mas.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.huace.mas.bean.DiffData;
import com.huace.mas.bean.KpiData;
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

	private Map<Integer, DiffData> diffMap = new ConcurrentHashMap<Integer, DiffData>();

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
		List<Project> result = new ArrayList<Project>();
		Map<String, String> kpiMap = springProperty.getKpiMap();
		List<String> kpis = new ArrayList<String>();
		List<String> kpiNames = new ArrayList<String>();
		for (String kpi : kpiMap.keySet()) {
			kpis.add(kpi);
			kpiNames.add(kpiMap.get(kpi));
		}
		for (Project p : projects) {
			p.setKpis(kpis);
			p.setKpiNames(kpiNames);
			result.add(p);
		}
		return result;
	}

	public void init() {
		this.checkAndRefresh();
	}

	@SuppressWarnings("unchecked")
	public synchronized Map<String, List<Kpi>> checkAndRefresh() {

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

						surface.setInit_h(getDouble(type.elementText("init_h")));
						surface.setYellow_h(getDouble(type
								.elementText("yellow_h")));
						surface.setOrange_h(getDouble(type
								.elementText("orange_h")));
						surface.setRed_h(getDouble(type.elementText("red_h")));
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
					kpi.setYellow_value(getDouble(type.elementText("yellow_x")));
					kpi.setOrange_value(getDouble(type.elementText("orange_x")));
					kpi.setRed_value(getDouble(type.elementText("red_x")));
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
	public List<KpiData> queryKpiData(Long projectID, String kpiType,
			String pointName, Date start, Date end) {
		Kpi kpi = null;

		Map<String, List<Kpi>> data = checkAndRefresh();
		Project project = dataDao.getProjectByID(projectID);
		if (project == null) {
			return new ArrayList<KpiData>();
		}
		List<Kpi> kpis = data.get(project.getProjectName());
		for (Kpi k : kpis) {
			if (k.get__type().startsWith(kpiType)
					&& k.getPointName().equals(pointName)) {
				kpi = k;
				break;
			}
		}
		if (kpi == null) {
			return new ArrayList<KpiData>();
		}
		List<KpiData> result = new ArrayList<KpiData>();
		try {
			List<Kpi> kpiDatas = this.dataDao.queryHistData(kpi.getDeviceID(),
					Class.forName("com.huace.mas.entity." + kpiType), start,
					end);
			for (Kpi k : kpiDatas) {
				KpiData kd = new KpiData();
				kd.setDacTime(k.getDacTime());
				if (kpi instanceof Surface) {
					if (((Surface) k).getV1() != null) {
						kd.setV1(((Surface) k).getV1()
								- ((Surface) kpi).getInit_x());
					}
					if (((Surface) k).getV2() != null) {
						kd.setV2(((Surface) k).getV2()
								- ((Surface) kpi).getInit_y());
					}
					if (((Surface) k).getV3() != null) {
						kd.setV3(((Surface) k).getV3()
								- ((Surface) kpi).getInit_h());
					}
					kd.setD2(Math.sqrt(kd.getV1() * kd.getV1() + kd.getV2()
							* kd.getV2()));
					kd.setD3(Math.sqrt(kd.getV1() * kd.getV1() + kd.getV2()
							* kd.getV2() + kd.getV3() * kd.getV3()));
				} else if (kpi instanceof Inner) {
					if (((Inner) k).getV1() != null) {
						kd.setV1(((Inner) k).getV1()
								- ((Inner) kpi).getInit_x());
					}
					if (((Inner) k).getV2() != null) {
						kd.setV2(((Inner) k).getV2()
								- ((Inner) kpi).getInit_y());
					}
				} else {
					kd.setV1(k.getV1());
				}
				result.add(kd);
			}
		} catch (Exception e) {
			log.error("", e);
		}

		return result;
	}

	@Override
	public List<List<Object>> getKpisForProject(Long projectID) {
		Map<String, List<Kpi>> data = checkAndRefresh();
		Project project = dataDao.getProjectByID(projectID);
		if (project == null) {
			return new ArrayList<List<Object>>();
		}
		List<Kpi> kpis = data.get(project.getProjectName());
		if (kpis == null || kpis.size() == 0) {
			return new ArrayList<List<Object>>();
		}
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
				if (s.getV1() != null) {
					s.setDis_x(s.getV1() - s.getInit_x());
				}
				if (s.getV2() != null) {
					s.setDis_y(s.getV2() - s.getInit_y());
				}
				if (s.getV3() != null) {
					s.setDis_h(s.getV3() - s.getInit_h());
				}
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

				if (s.getDis_y() != null) {
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
				}

				if (s.getDis_h() != null) {
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
				}

				s.setAlertGrade(s.getAlertGrade_x());
				if (s.getAlertGrade_y() > s.getAlertGrade()) {
					s.setAlertGrade(s.getAlertGrade_y());
				}
				if (s.getAlertGrade_h() > s.getAlertGrade()) {
					s.setAlertGrade(s.getAlertGrade_h());
				}

				s.setD2(Math.sqrt(s.getDis_x() * s.getDis_x() + s.getDis_y() * s.getDis_y()));
				s.setD3(Math.sqrt(s.getDis_x() * s.getDis_x() + s.getDis_y() * s.getDis_y() + s.getDis_h() * s.getDis_h()));
				DiffData diffData = diffMap.get(kpi.getDeviceID());
				if (diffData == null || !diffData.isSameToday(s.getDacTime())) {
					diffData = this.getDiffData(s, DateUtils.truncate(
							s.getDacTime(), Calendar.DAY_OF_MONTH));
					diffMap.put(kpi.getDeviceID(), diffData);
				}
				s.setTodayChangeValue(getDiff(s,diffData,1));
				s.setYesterdayChangeValue(getDiff(s,diffData,2));
				s.setWeekChangeValue(getDiff(s,diffData,7));
			} else if (kpi instanceof Inner) {
				((Inner) kpi).setV2(((Inner) dbKpi).getV2());

				Inner s = (Inner) kpi;
				if (s.getV1() != null) {
					s.setDis_x(s.getV1() - s.getInit_x());
				}
				if (s.getV2() != null) {
					s.setDis_y(s.getV2() - s.getInit_y());
				}

				// 数据越大告警越大
				if (s.getDis_x() != null) {
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
				}
				if (s.getDis_y() != null) {
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

	private Double getDiff(Surface surface, DiffData diffData,int type) {
		if(type==1){
			return Math.sqrt((surface.getV1() - diffData.getV1Today())
					* (surface.getV1() - diffData.getV1Today())
					+ (surface.getV2() - diffData.getV2Today())
					* (surface.getV2() - diffData.getV2Today())
					+ (surface.getV3() - diffData.getV3Today())
					* (surface.getV3() - diffData.getV3Today()));
		}else if(type==2){
			return Math.sqrt((surface.getV1() - diffData.getV1Yesterday())
					* (surface.getV1() - diffData.getV1Yesterday())
					+ (surface.getV2() - diffData.getV2Yesterday())
					* (surface.getV2() - diffData.getV2Yesterday())
					+ (surface.getV3() - diffData.getV3Yesterday())
					* (surface.getV3() - diffData.getV3Yesterday()));
		}else if(type==7){
			return Math.sqrt((surface.getV1() - diffData.getV1Week())
					* (surface.getV1() - diffData.getV1Week())
					+ (surface.getV2() - diffData.getV2Week())
					* (surface.getV2() - diffData.getV2Week())
					+ (surface.getV3() - diffData.getV3Week())
					* (surface.getV3() - diffData.getV3Week()));
		}else{
			return 0.0;
		}
		
	}

	private DiffData getDiffData(Surface surface, Date today) {
		Date end = today;
		Date start = DateUtils.addDays(today, -7);
		DiffData diffData = new DiffData(today);
		List<Kpi> kpis = this.dataDao.queryHistData(surface.getDeviceID(),
				surface.getClass(), start, end);
		for (Kpi dbKpi : kpis) {
			Date d = dbKpi.getDacTime();
			Surface dbSurface = (Surface) dbKpi;
			if (d.compareTo(DateUtils.addDays(today, -1)) > 0) {
				// 昨天的数据
				diffData.setV1Today(dbSurface.getV1());
				diffData.setV2Today(dbSurface.getV2());
				diffData.setV3Today(dbSurface.getV3());
			}
			if (d.compareTo(DateUtils.addDays(today, -2)) > 0
					&& d.compareTo(DateUtils.addDays(today, -1)) < 0) {
				// 前天的数据
				diffData.setV1Yesterday(dbSurface.getV1());
				diffData.setV2Yesterday(dbSurface.getV2());
				diffData.setV3Yesterday(dbSurface.getV3());
			}
			if (d.compareTo(DateUtils.addDays(today, -7)) > 0
					&& d.compareTo(DateUtils.addDays(today, -6)) < 0) {
				// 7天前的数据
				diffData.setV1Week(dbSurface.getV1());
				diffData.setV2Week(dbSurface.getV2());
				diffData.setV3Week(dbSurface.getV3());
			}

		}
		return diffData;
	}
}
