/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author dapeng.zhang
 *
 */
package com.clican.pluto.fsm.interceptor;

import java.io.Serializable;
import java.util.Map;

import com.clican.pluto.fsm.enumeration.EventType;

/**
 * 给一个状态发送事件时候的拦截器
 * 
 * @author dapeng.zhang
 * 
 */
public interface EventDispatchInterceptor {
	/**
	 * 在发送一个事件之前，对特定类型的流程状态进行parameters的替换。
	 * <p>
	 * 例如:审批报告任务状态发送事件时，根据审批人当前的状态找到合适的审批人。
	 * 
	 * @param sessionId
	 *            流程的Id
	 * @param stateId
	 *            状态的Id
	 * @param eventType
	 *            事件类型
	 * @param parameters
	 *            事件参数
	 */
	public void beforeDispatch(Long sessionId, Long stateId, EventType eventType, Map<String, Serializable> parameters);

	/**
	 * 完成发送事件后，需要执行的代码
	 * 
	 * @param sessionId
	 * @param stateId
	 * @param eventType
	 * @param parameters
	 */
	public void afterDispatch(Long sessionId, Long stateId, EventType eventType, Map<String, Serializable> parameters);

}

// $Id$