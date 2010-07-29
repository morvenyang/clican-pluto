/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener;

import com.clican.pluto.fsm.model.Task;

/**
 * 创建任务的监听器
 * 
 * @author xiaoming.lu
 * 
 */
public interface TaskListener {

	/**
	 * 在保存task之前调用
	 */
	void beforeTask(Task task);

	/**
	 * 在保存task之后调用
	 */
	void afterTask(Task task);
}

// $Id$