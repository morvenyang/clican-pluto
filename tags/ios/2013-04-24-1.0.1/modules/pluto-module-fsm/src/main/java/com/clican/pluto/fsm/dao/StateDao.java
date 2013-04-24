/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao;

import java.io.Serializable;
import java.util.List;

import com.clican.pluto.common.dao.CommonDao;
import com.clican.pluto.fsm.model.State;

/**
 * 该接口主要用于处理和状态对象相关的数据操作
 * 
 * @author wei.zhang
 * 
 */
public interface StateDao extends CommonDao {

	/**
	 * 设置State Scope的变量,并持久化到数据库
	 * 
	 * @param state
	 *            State对象
	 * @param name
	 *            变量名
	 * @param value
	 *            变量值
	 */
	public void setVariable(State state, String name, Serializable value);

	/**
	 * 返回当前<code>Session</code>的处于激活状态的状态基集合
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<State> getActiveStates(Long sessionId);

	/**
	 * 返回当前<code>Session</code>的处于激活或等待状态的状态基集合
	 * @param sessionId
	 * @return
	 */
	public List<State> getActiveAndPendingState(Long sessionId);

	/**
	 * 返回等待状态的状态基
	 * 
	 * @param sessionId
	 * @param stateName
	 * @return
	 */
	public State getPendingState(Long sessionId, String stateName);

	/**
	 * 根据id查找State
	 * 
	 * @param stateId
	 * @return
	 */
	public State findStateById(Long stateId);

	/**
	 * 把所有的状态基的状态都设置为INACTIVE
	 * 
	 * @param sessionId
	 */
	public void inactiveStatesBySessionId(Long sessionId);

	/**
	 * 根据sessionId把所有罗列的stateNames中的state设置成INACTIVE
	 * 
	 * @param sessionId
	 * @param stateNames
	 */
	public void inactiveState(Long sessionId, String[] stateNames);
}

// $Id$