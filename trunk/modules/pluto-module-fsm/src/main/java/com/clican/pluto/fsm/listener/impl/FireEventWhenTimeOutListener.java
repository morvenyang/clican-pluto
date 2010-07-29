/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.clican.pluto.fsm.engine.EventDispatcher;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.listener.BaseTimeOutListener;
import com.clican.pluto.fsm.model.Job;

/**
 * 默认的如果任务超时执行的Listener
 * 
 * @author wei.zhang
 * 
 */
public class FireEventWhenTimeOutListener extends BaseTimeOutListener {

	/**
	 * @see EventDispatcher
	 */
	private EventDispatcher eventDispatcher;

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	
	public void onTimeOut(Job job) {
		if (log.isDebugEnabled()) {
			log.debug("timeout job [" + job.getId() + "] of  state[" + job.getState().getName() + "] start to execute...");
		}
		eventDispatcher.dispatch(job.getState().getSession().getId(), job.getState().getId(), EventType.JOB, getParameters());
	}

	public Map<String, Serializable> getParameters() {
		return new HashMap<String, Serializable>();
	}
}

// $Id$