/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import java.util.Date;
import java.util.List;

import com.clican.pluto.fsm.enumeration.JobStatus;
import com.clican.pluto.fsm.model.Job;
import com.clican.pluto.fsm.model.State;

/**
 * 处理Job相关的带有事务的新建/修改/删除/查找。
 * 
 * @author wei.zhang
 * 
 */
public interface JobService {

	/**
	 * 保存一个任务到数据库中
	 * 
	 * @param job
	 */
	public void saveJob(Job job);

	/**
	 * 更新一个数据库中的任务
	 * 
	 * @param job
	 */
	public void updateJob(Job job);

	/**
	 * 删除一个数据库中的任务
	 * 
	 * @param job
	 */
	public void deleteJob(Job job);

	/**
	 * 根据任务id来查找任务
	 * 
	 * @param jobId
	 * @return
	 */
	public Job findJobById(Long jobId);

	/**
	 * 查找所有执行时间小于参数执行时间的任务
	 * 
	 * @param executeTime
	 * @param status
	 * @return
	 */
	public List<Job> findJobByExecuteTime(Date executeTime, JobStatus status);

	/**
	 * 查找当前State所属的还未执行的Job
	 * 
	 * @param state
	 * @return
	 */
	public List<Job> findIdelJobsByState(State state);

}

// $Id$