/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import com.clican.pluto.fsm.model.Job;

/**
 * Job任务的容器类。用于管理和调度所有工作流系统的Job任务。
 * 
 * @author wei.zhang
 * 
 */
public interface JobContext {

	/**
	 * 添加一个任务
	 * 
	 * @param job
	 */
	public void addJob(Job job);

	/**
	 * 加载所有的将要执行的任务
	 */
	public void loadJobs();

	/**
	 * 删除一个任务，如果该任务还为执行则永远不会执行，如果在执行中，则直接删除数据库中数据。
	 * 
	 * @param job
	 */
	public void removeJob(Job job);

	/**
	 * 立即执行一个任务
	 * 
	 * @param job
	 */
	public void executeJob(Job job);

	/**
	 * 启动的时候要求把所有原来正在执行的任务全部都加载进来
	 */
	public void start();

	/**
	 * 系统停止的时候要求能正常的结束相关的Job
	 */
	public void stop();

}

// $Id$