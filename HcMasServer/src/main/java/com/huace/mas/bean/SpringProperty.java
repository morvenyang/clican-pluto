package com.huace.mas.bean;

public class SpringProperty {

	
	private String apnsCertFile;
	private String apnsCertPassword;
	private String apnsCertEnv;
	
	private boolean systemProxyEnable;
	private String systemProxyHost;
	private int systemProxyPort;
	
	private String alertConfigXmlPath;

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

	
}
