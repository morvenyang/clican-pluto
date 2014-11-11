package com.huace.mas.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huace.mas.apns.ApnsService;
import com.huace.mas.bean.SpringProperty;
import com.huace.mas.bean.Token;
import com.huace.mas.dao.DataDao;
import com.huace.mas.entity.DryBeach;
import com.huace.mas.entity.Inner;
import com.huace.mas.entity.Kpi;
import com.huace.mas.entity.Project;
import com.huace.mas.entity.Saturation;
import com.huace.mas.entity.Surface;
import com.huace.mas.service.DataService;
import com.huace.mas.service.PushService;

public class PushServiceImpl implements PushService {

	private final static Log log = LogFactory.getLog(PushServiceImpl.class);

	private DataDao dataDao;

	private ApnsService apnsService;

	private DataService dataService;

	private Map<String, List<Token>> tokens;

	private SpringProperty springProperty;

	private AtomicLong alertSequence = new AtomicLong(1);

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	public void setApnsService(ApnsService apnsService) {
		this.apnsService = apnsService;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	@SuppressWarnings("unchecked")
	public void init() {
		tokens = new ConcurrentHashMap<String, List<Token>>();
		try {
			File file = new File(springProperty.getTokenFile());
			if (file.exists()) {
				String str = new String(FileUtils.readFileToByteArray(new File(
						springProperty.getTokenFile())), "utf-8");
				Map<String, JSONArray> map = new HashMap<String, JSONArray>(
						JSONObject.fromObject(str));
				for (String userName : map.keySet()) {
					JSONArray jsonArray = map.get(userName);
					if (!tokens.containsKey(userName)) {
						tokens.put(userName, new ArrayList<Token>());
					}
					for (Object obj : jsonArray) {
						JSONObject jsonObj = (JSONObject) obj;
						Token t = (Token) JSONObject.toBean(jsonObj,
								Token.class);
						tokens.get(userName).add(t);
					}
				}
			} else {
				file.getParentFile().mkdirs();
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public void pushMsg() {
		if (log.isInfoEnabled()) {
			log.info("start push message");
		}
		Map<String, List<String>> messages = this.generateMessages();
		for (String token : messages.keySet()) {
			List<String> ms = messages.get(token);
			for (String message : ms) {
				try {
					if (log.isInfoEnabled()) {
						log.info("push message[" + message + "] to token ["
								+ token + "]");
					}
					apnsService.sendMessage(message, token);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	private Map<String, List<String>> generateMessages() {
		Map<String, List<String>> messages = new HashMap<String, List<String>>();
		Map<String, List<Kpi>> data = dataService.checkAndRefresh();
		List<Project> projects = dataDao.findAllProjects();
		Map<Long, List<String>> projectTokenMap = new HashMap<Long, List<String>>();
		for (String userName : tokens.keySet()) {
			List<Token> ts = tokens.get(userName);
			for (Token t : ts) {
				if (!projectTokenMap.containsKey(t.getProjectID())) {
					projectTokenMap.put(t.getProjectID(),
							new ArrayList<String>());
				}
				projectTokenMap.get(t.getProjectID()).add(t.getToken());
			}
		}
		for (Project project : projects) {
			List<Kpi> kpis = data.get(project.getProjectName());
			List<String> ts = projectTokenMap.get((long) project.getId());
			if (ts == null) {
				continue;
			}
			for (Kpi kpi : kpis) {
				Kpi dbKpi = this.dataDao.queryDataByDeviceID(kpi.getDeviceID(),
						kpi.getClass());
				if (dbKpi == null) {
					continue;
				}
				long sequence = alertSequence.getAndIncrement();
				if (dbKpi instanceof Surface) {
					Surface s = (Surface) dbKpi;
					Surface metaSurface = (Surface) kpi;
					if (s.getV1() != null) {
						s.setDis_x(s.getV1() - metaSurface.getInit_x());
					}
					if (s.getV2() != null) {
						s.setDis_y(s.getV2() - metaSurface.getInit_y());
					}
					if (s.getV3() != null) {
						s.setDis_h(s.getV3() - metaSurface.getInit_h());
					}
					// 数据越大告警越大
					if (s.getDis_x() >= metaSurface.getRed_x()) {
						s.setAlertGrade_x(3);
					} else if (s.getDis_x() < metaSurface.getRed_x()
							&& s.getV1() >= metaSurface.getOrange_x()) {
						s.setAlertGrade_x(2);
					} else if (s.getDis_x() < metaSurface.getOrange_x()
							&& s.getV1() >= metaSurface.getYellow_x()) {
						s.setAlertGrade_x(1);
					} else {
						s.setAlertGrade_x(0);
					}

					if (s.getDis_y() != null) {
						if (s.getDis_y() >= metaSurface.getRed_y()) {
							s.setAlertGrade_y(3);
						} else if (s.getDis_y() < metaSurface.getRed_y()
								&& s.getV1() >= metaSurface.getOrange_y()) {
							s.setAlertGrade_y(2);
						} else if (s.getDis_y() < metaSurface.getOrange_y()
								&& s.getV1() >= metaSurface.getYellow_y()) {
							s.setAlertGrade_y(1);
						} else {
							s.setAlertGrade_y(0);
						}
					}

					if (s.getDis_h() != null) {
						if (s.getDis_h() >= metaSurface.getRed_h()) {
							s.setAlertGrade_h(3);
						} else if (s.getDis_h() < metaSurface.getRed_h()
								&& s.getV1() >= metaSurface.getOrange_h()) {
							s.setAlertGrade_h(2);
						} else if (s.getDis_h() < metaSurface.getOrange_h()
								&& s.getV1() >= metaSurface.getYellow_h()) {
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
				} else if (dbKpi instanceof Inner) {
					Inner inner = (Inner) dbKpi;
					Inner metaInner = (Inner) kpi;
					if (inner.getV1() != null) {
						inner.setDis_x(inner.getV1() - metaInner.getInit_x());
					}
					if (inner.getV2() != null) {
						inner.setDis_y(inner.getV2() - metaInner.getInit_y());
					}

					// 数据越大告警越大
					if (inner.getDis_x() != null) {
						if (inner.getDis_x() >= metaInner.getRed_x()) {
							inner.setAlertGrade_x(3);
						} else if (inner.getDis_x() < metaInner.getRed_x()
								&& inner.getV1() >= metaInner.getOrange_x()) {
							inner.setAlertGrade_x(2);
						} else if (inner.getDis_x() < metaInner.getOrange_x()
								&& inner.getV1() >= metaInner.getYellow_x()) {
							inner.setAlertGrade_x(1);
						} else {
							inner.setAlertGrade_x(0);
						}
					}
					if (inner.getDis_y() != null) {
						if (inner.getDis_y() >= metaInner.getRed_y()) {
							inner.setAlertGrade_y(3);
						} else if (inner.getDis_y() < metaInner.getRed_y()
								&& inner.getV1() >= metaInner.getOrange_y()) {
							inner.setAlertGrade_y(2);
						} else if (inner.getDis_y() < metaInner.getOrange_y()
								&& inner.getV1() >= metaInner.getYellow_y()) {
							inner.setAlertGrade_y(1);
						} else {
							inner.setAlertGrade_y(0);
						}
					}

					inner.setAlertGrade(inner.getAlertGrade_x());
					if (inner.getAlertGrade_y() > inner.getAlertGrade()) {
						inner.setAlertGrade(inner.getAlertGrade_y());
					}
				} else if (dbKpi instanceof Saturation
						|| dbKpi instanceof DryBeach) {
					// 数据越小告警越大
					if (dbKpi.getV1() <= kpi.getRed_value()) {
						dbKpi.setAlertGrade(3);
					} else if (dbKpi.getV1() > kpi.getRed_value()
							&& dbKpi.getV1() <= kpi.getOrange_value()) {
						dbKpi.setAlertGrade(2);
					} else if (dbKpi.getV1() > kpi.getOrange_value()
							&& dbKpi.getV1() <= kpi.getYellow_value()) {
						dbKpi.setAlertGrade(1);
					} else {
						dbKpi.setAlertGrade(0);
					}
				} else {
					// 数据越大告警越大
					if (dbKpi.getV1() >= kpi.getRed_value()) {
						dbKpi.setAlertGrade(3);
					} else if (dbKpi.getV1() < kpi.getRed_value()
							&& dbKpi.getV1() >= kpi.getOrange_value()) {
						dbKpi.setAlertGrade(2);
					} else if (dbKpi.getV1() < kpi.getOrange_value()
							&& dbKpi.getV1() >= kpi.getYellow_value()) {
						dbKpi.setAlertGrade(1);
					} else {
						dbKpi.setAlertGrade(0);
					}
				}
				if (dbKpi.getAlertGrade() > 0 && kpi.isAlert()) {
					String kpiType = dbKpi.getClass().getSimpleName();
					if (StringUtils.isEmpty(springProperty.getKpiMap().get(
							kpiType))) {
						continue;
					}
					String message = springProperty.getKpiMap().get(kpiType);
					if (dbKpi.getAlertGrade() == 1) {
						message += "【黄色】";
					} else if (dbKpi.getAlertGrade() == 2) {
						message += "【橙色】";
					} else if (dbKpi.getAlertGrade() == 3) {
						message += "【红色】";
					}
					if (log.isDebugEnabled()) {
						log.debug(sequence + "-Alert message:" + message);
					}
					if (kpi.getLastAlertTime() == null
							|| dbKpi.getDacTime().compareTo(
									kpi.getLastAlertTime()) > 0) {
						if (log.isDebugEnabled()) {
							log.debug(sequence
									+ "-This is new alert message, we shall send it");
						}
						kpi.setLastAlertTime(dbKpi.getDacTime());
						for (String t : ts) {
							if (!messages.containsKey(t)) {
								messages.put(t, new ArrayList<String>());
							}
							messages.get(t).add(message);
						}
					} else {
						if (log.isDebugEnabled()) {
							log.debug(sequence
									+ "-This is old alert message, we don't send it");
						}
					}

				}
			}
		}
		return messages;
	}

	@Override
	public synchronized void registerToken(String userName, String token,
			Long projectID) {
		if (log.isDebugEnabled()) {
			log.debug("register token[" + token + "] for user [" + userName
					+ "]");
		}
		Token t = new Token();
		t.setUserName(userName);
		t.setToken(token);
		t.setProjectID(projectID);
		List<Token> oldTokens = tokens.get(userName);
		if (oldTokens != null) {
			for (Token oldToken : oldTokens) {
				if (oldToken.getToken().contains(token)) {
					return;
				}
			}
		}
		if (!tokens.containsKey(userName)) {
			tokens.put(userName, new ArrayList<Token>());
		}
		tokens.get(userName).add(t);
		try {
			FileUtils.writeByteArrayToFile(
					new File(springProperty.getTokenFile()), JSONObject
							.fromObject(tokens).toString().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}

	}

}
