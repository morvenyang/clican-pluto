/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener;

import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Task;

/**
 * 创建任务的监听器
 * 
 * @author clican
 * 
 */
public interface TaskListener {

	/**
	 * 在分配task之前调用
	 */
	public boolean beforeAssignTask(Task task);

	/**
	 * 在分配task之后调用
	 */
	public void afterAssignTask(Task task);

	/**
	 * 在处理任务前被调用
	 * 
	 * @param task
	 */
	public void beforeHandleTask(Task task, Event event);

	/**
	 * 在处理任务后被调用
	 * 
	 * @param task
	 */
	public void afterHandleTask(Task task, Event event);

}

// $Id$