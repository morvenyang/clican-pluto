/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.bean;

import java.io.Serializable;

/**
 * 配置部署的数据处理组的描述
 * 
 * @author clican
 * 
 */
public class Deploy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5436810274814671257L;

	/**
	 * 数据处理组的名称
	 */
	private String name;

	/**
	 * 数据处理组的描述文件的url
	 */
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	private String propertyResources;

	public String getPropertyResources() {
		return propertyResources;
	}

	public void setPropertyResources(String propertyResources) {
		this.propertyResources = propertyResources;
	}



}

// $Id: Deploy.java 16256 2010-07-16 08:35:53Z wei.zhang $