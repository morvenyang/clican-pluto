/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.bean;

import java.io.Serializable;


/**
 * 配置部署的工作流描述
 * 
 * @author wei.zhang
 * 
 */
public class Deploy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5436810274814671257L;

	/**
	 * 工作流名称
	 */
	private String name;

	/**
	 * 工作流版本
	 */
	private int version;
	/**
	 * 工作流描述文件的url
	 */
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

// $Id$