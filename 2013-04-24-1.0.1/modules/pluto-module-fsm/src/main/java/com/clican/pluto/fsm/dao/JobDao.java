/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao;

import java.util.Date;
import java.util.List;

import com.clican.pluto.common.dao.CommonDao;
import com.clican.pluto.fsm.enumeration.JobStatus;
import com.clican.pluto.fsm.model.Job;
import com.clican.pluto.fsm.model.State;

/**
 * 该接口主要用来操作和任务<code>Job</code>相关的数据对象的操作
 * 
 * @author wei.zhang
 * 
 */
public interface JobDao extends CommonDao {

	/**
	 * 查找所有执行时间小于参数执行时间的任务。
	 * 
	 * @param executeTime
	 * @param status
	 * @return
	 */
	public List<Job> findJobByExecuteTime(Date executeTime, JobStatus status);

	/**
	 * 根据任务id查找相应的任务。
	 * 
	 * @param jobId
	 * @return
	 */
	public Job findJobById(Long jobId);

	/**
	 * 查找相应<code>State</code>下所有还未执行的任务。
	 * 
	 * @param state
	 * @return
	 */
	public List<Job> findIdelJobsByState(State state);

	/**
	 * 根据sessionId删除所有的任务
	 * 
	 * @param sessionId
	 * @return
	 */
	public void deleteJobsBySessionId(Long sessionId);

	/**
	 * 根据stateId删除所有的任务
	 * 
	 * @param stateId
	 * @return
	 */
	public void deleteJobsByStateId(Long stateId);
}

// $Id$