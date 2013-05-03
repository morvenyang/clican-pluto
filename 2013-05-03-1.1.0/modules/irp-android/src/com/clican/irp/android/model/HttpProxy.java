package com.clican.irp.android.model;

import java.io.Serializable;

public class HttpProxy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7621611920239411768L;

	private String proxyHost;
	
	private Integer proxyPort;
	
	private String proxyUserName;
	
	private String proxyPassword;

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUserName() {
		return proxyUserName;
	}

	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
	
	
}
