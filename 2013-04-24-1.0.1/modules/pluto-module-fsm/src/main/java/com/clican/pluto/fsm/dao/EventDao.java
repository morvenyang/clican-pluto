/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao;

import java.io.Serializable;

import com.clican.pluto.common.dao.CommonDao;
import com.clican.pluto.fsm.model.Event;

/**
 * 该接口主要用来操作和事件<code>Event</code>相关的数据对象的操作
 * 
 * @author wei.zhang
 * 
 */
public interface EventDao extends CommonDao {

	/**
	 * 设置Event Scope的变量,并持久化到数据库
	 * 
	 * @param event
	 *            Event对象
	 * @param name
	 *            变量名
	 * @param value
	 *            变量值
	 */
	public void setVariable(Event event, String name, Serializable value);
}

// $Id$