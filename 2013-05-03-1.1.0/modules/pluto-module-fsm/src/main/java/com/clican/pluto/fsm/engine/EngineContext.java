/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import java.io.Serializable;
import java.util.List;

import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;

/**
 * 工作流引擎的主要接口类。通过该接口类能够用于处理和<code>Session</code>,<code>Task</code>相关的业务流程处理。
 * <p>
 * 比如新建/删除/查询<code>Session</code>
 * <p>
 * 查询<code>Task</code>
 * 
 * @author wei.zhang
 * 
 */
public interface EngineContext {

	/**
	 * 根据sessionId来查找对应的<code>Session</code>对象
	 * 
	 * @param sessionId
	 * @return
	 */
	public Session querySession(Long sessionId);

	/**
	 * 根据taskId来查找对应的<code>Task</code>对象
	 * 
	 * @param taskId
	 * @return
	 */
	public Task queryTask(Long taskId);

	/**
	 * 根据sessionName创建一个新的<code>Session</code>
	 * 
	 * @param sessionName
	 * @param userId
	 *            工作流的发起者
	 * @return
	 */
	public Session newSession(String sessionName, String userId);

	/**
	 * 创建一个子工作流
	 * 
	 * @param parentSessionId
	 * @param subStateName
	 * @return
	 */
	public Session newSubSession(Long parentSessionId, String subStateName);

	/**
	 * 根据sessionName和stateName来取得当前active的<code>IState</code>
	 * 
	 * @param sessionName
	 * @param sessionVersion
	 * @param stateName
	 * @return
	 */
	public IState getState(String sessionName, int sessionVersion, String stateName);

	/**
	 * 根据stateId来查找对应的<code>State</code>
	 * 
	 * @param stateId
	 * @return
	 */
	public State findStateById(Long stateId);

	/**
	 * 根据userId, sessionName和stateName来查询所有相关的任务。
	 * 
	 * @param userId
	 * @param sessionName
	 * @param stateName
	 * @param completed
	 * @return
	 */
	public List<Task> queryTask(String userId, String sessionName, String stateName, boolean completed);

	/**
	 * 根据sessionId来查找当前Active的state
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<State> getActiveState(Long sessionId);

	/**
	 * 返回当前<code>Session</code>的处于激活或等待状态的状态基集合
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<State> getActiveAndPendingState(Long sessionId);

	/**
	 * 删除一个Session以及其相关的所有状态，任务和变量
	 * 
	 * @param sessionId
	 */
	public void deleteSession(Long sessionId);

	/**
	 * 查询当前Session的一个变量值
	 * 
	 * @param sessionId
	 * @param variableName
	 *            变量名
	 * @return
	 */
	public Serializable getVariableValue(Long sessionId, String variableName);

	/**
	 * 强制结束一个session包括其所有的state,task,job
	 * <p>
	 * 强制结束后不会回调任何的startListener,endListener,timeoutListener
	 * 
	 * @param sessionId
	 */
	public void completeSession(Long sessionId);

}

// $Id$