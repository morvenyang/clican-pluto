/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.fsm.listener;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The abstract Listener base class. Used to provide default interface
 * implementation.
 * <p>
 * Listener的基类.用于提供接口的默认实现.
 * 
 * @author clican
 * 
 */
public abstract class BaseListener implements Listener {

	/**
	 * The Log handler
	 * <p>
	 * 日志实例
	 */
	protected final Log log = LogFactory.getLog(getClass());
	/**
	 * Used to hold param
	 * <p>
	 * 用来保存参数
	 */
	private Map<String, Serializable> param;

	public Map<String, Serializable> getParam() {
		return param;
	}

	public void setParam(Map<String, Serializable> param) {
		this.param = param;
	}

}

// $Id$