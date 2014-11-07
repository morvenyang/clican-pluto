package com.huace.mas.bean;

import java.util.HashMap;
import java.util.Map;

public class SpringProperty {

	
	private String apnsCertFile;
	private String apnsCertPassword;
	private String apnsCertEnv;
	
	private boolean systemProxyEnable;
	private String systemProxyHost;
	private int systemProxyPort;
	
	private String alertConfigXmlPath;
	
	private String tokenFile;
	
	private String projectName;
	
	private Map<String,Integer> orderMap = new HashMap<String,Integer>();
	private Map<String,String> kpiMap = new HashMap<String,String>();

	public boolean isSystemProxyEnable() {
		return systemProxyEnable;
	}

	public void setSystemProxyEnable(boolean systemProxyEnable) {
		this.systemProxyEnable = systemProxyEnable;
	}

	public String getApnsCertEnv() {
		return apnsCertEnv;
	}

	public void setApnsCertEnv(String apnsCertEnv) {
		this.apnsCertEnv = apnsCertEnv;
	}

	public String getSystemProxyHost() {
		return systemProxyHost;
	}

	public void setSystemProxyHost(String systemProxyHost) {
		this.systemProxyHost = systemProxyHost;
	}

	public int getSystemProxyPort() {
		return systemProxyPort;
	}

	public void setSystemProxyPort(int systemProxyPort) {
		this.systemProxyPort = systemProxyPort;
	}

	public String getApnsCertPassword() {
		return apnsCertPassword;
	}

	public void setApnsCertPassword(String apnsCertPassword) {
		this.apnsCertPassword = apnsCertPassword;
	}

	public String getApnsCertFile() {
		return apnsCertFile;
	}

	public void setApnsCertFile(String apnsCertFile) {
		this.apnsCertFile = apnsCertFile;
	}

	public String getAlertConfigXmlPath() {
		return alertConfigXmlPath;
	}

	public void setAlertConfigXmlPath(String alertConfigXmlPath) {
		this.alertConfigXmlPath = alertConfigXmlPath;
	}

	public Map<String, Integer> getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map<String, Integer> orderMap) {
		this.orderMap = orderMap;
	}

	public void setOrderMapStr(String orderMapStr) {
		String[] maps = orderMapStr.split(";");
		for(String map:maps){
			String key = map.split(":")[0];
			String value = map.split(":")[1];
			this.orderMap.put(key, Integer.parseInt(value));
		}
	}

	public Map<String, String> getKpiMap() {
		return kpiMap;
	}

	public void setKpiMap(Map<String, String> kpiMap) {
		this.kpiMap = kpiMap;
	}
	
	public void setKpiMapStr(String orderMapStr) {
		String[] maps = orderMapStr.split(";");
		for(String map:maps){
			String key = map.split(":")[0];
			String value = map.split(":")[1];
			this.kpiMap.put(key, value);
		}
	}

	public String getTokenFile() {
		return tokenFile;
	}

	public void setTokenFile(String tokenFile) {
		this.tokenFile = tokenFile;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
}
