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
import com.clican.pluto.fsm.model.Task;

/**
 * 该接口主要用于处理和任务相关的数据操作
 * 
 * @author wei.zhang
 * 
 */
public interface TaskDao extends CommonDao {

	/**
	 * 查找当前状态基下对应的任务。
	 * 
	 * @param state
	 *            状态基对象
	 * @return
	 */
	public List<Task> findActiveTasksByState(State state);

	/**
	 * 根据下列参数查找任务
	 * 
	 * @param userId
	 *            用户assignee的id
	 * @param sessionName
	 *            session的名称,即工作流的名称
	 * @param stateName
	 *            状态基的名称
	 * @param completed
	 *            该任务是否已经完成
	 * @return
	 */
	public List<Task> findTasksByParams(String userId, String sessionName,
			String stateName, boolean completed);

	/**
	 * 根据任务id来查找对应的任务
	 * 
	 * @param taskId
	 * @return
	 */
	public Task findTaskById(Long taskId);

	/**
	 * 设置Task Scope的变量,并持久化到数据库
	 * 
	 * @param task
	 *            Task对象
	 * @param name
	 *            变量名
	 * @param value
	 *            变量值
	 */
	public void setVariable(Task task, String name, Serializable value);

	/**
	 * 结束当前Session所对应的所有的任务
	 * 
	 * @param sessionId
	 */
	public void completeTasksBySessionId(Long sessionId);

	/**
	 * 结束当前State所对应的所有的任务
	 * 
	 * @param stateId
	 */
	public void completeTasksByStateId(Long stateId);


}

// $Id$