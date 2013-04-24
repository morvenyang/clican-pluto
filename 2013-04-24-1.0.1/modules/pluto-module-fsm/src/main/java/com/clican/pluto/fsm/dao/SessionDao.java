/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao;

import java.io.Serializable;

import com.clican.pluto.common.dao.CommonDao;
import com.clican.pluto.fsm.model.Session;

/**
 * 该接口主要用于和<code>Session</code>相关的数据对象的操作
 * 
 * @author wei.zhang
 * 
 */
public interface SessionDao extends CommonDao {

	/**
	 * 根据sessionId来查找对应的<code>Session</code>对象
	 * 
	 * @param sessionId
	 * @return
	 */
	public Session findSessionById(Long sessionId);

	/**
	 * 设置Session Scope的变量,并持久化到数据库
	 * 
	 * @param session
	 *            Session对象
	 * @param name
	 *            变量名
	 * @param value
	 *            变量值
	 */
	public void setVariable(Session session, String name, Serializable value);

	/**
	 * 根据SessionId来查找对应的变量
	 * 
	 * @param sessionId
	 * @param variableName
	 * @return
	 */
	public Serializable getVariableValue(Long sessionId, String variableName);
}

// $Id$