/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao;

import java.io.Serializable;
import java.util.Map;

import com.clican.pluto.common.dao.CommonDao;

/**
 * 该接口主要用于操作和变量有关的数据操作
 * 
 * @author wei.zhang
 * 
 */
public interface VariableDao extends CommonDao {

	/**
	 * 设置Session的变量
	 * 
	 * @param sessionId
	 *            Session编号
	 */
	Map<String, Serializable> getSessionVariableValues(Long sessionId);

}

// $Id$