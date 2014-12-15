package com.peacebird.dataserver.bean;

public class SpringProperty {

	private String version;
	private String serverUrl;
	
	private String apnsCertFile;
	private String apnsCertPassword;
	private String apnsCertEnv;
	
	private boolean systemProxyEnable;
	private String systemProxyHost;
	private int systemProxyPort;
	
	private String yesterday;
	private String imageUrlPrefix;
	
	private boolean mockValue = true;
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

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

	public String getYesterday() {
		return yesterday;
	}

	public void setYesterday(String yesterday) {
		this.yesterday = yesterday;
	}

	public String getImageUrlPrefix() {
		return imageUrlPrefix;
	}

	public void setImageUrlPrefix(String imageUrlPrefix) {
		this.imageUrlPrefix = imageUrlPrefix;
	}

	public boolean isMockValue() {
		return mockValue;
	}

	public void setMockValue(boolean mockValue) {
		this.mockValue = mockValue;
	}
	
	
}
