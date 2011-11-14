/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.common.bean;

public class SpringProperty {

	private String version;
	
	private String contextPath;
	private String applicationUrlPrefix;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getApplicationUrlPrefix() {
		return applicationUrlPrefix;
	}

	public void setApplicationUrlPrefix(String applicationUrlPrefix) {
		this.applicationUrlPrefix = applicationUrlPrefix;
	}
	
	
	
	
}


//$Id$