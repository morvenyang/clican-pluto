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

/**
 * This interface is used to define common method used by all of FSM Listener.
 * <p>
 * 本接口主要用来定义所有的在FSM中用来的Listener中的公共方法.
 * 
 * @author clican
 * 
 */
public interface Listener {

	/**
	 * set listener's init-param by xml configuration
	 * <p>
	 * 设置listener的初始化参数通过xml配置文件
	 * 
	 * @param param
	 */
	public void setParam(Map<String, Serializable> param);

	/**
	 * get listener's init-param
	 * <p>
	 * 返回listener的参数
	 * 
	 * @return
	 */
	public Map<String, Serializable> getParam();

}

// $Id$