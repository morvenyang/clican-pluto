/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import java.io.Serializable;
import java.util.Map;

import com.clican.pluto.fsm.enumeration.EventType;

/**
 * 该接口是工作流引擎的事件驱动接口。
 * <p>
 * 当有新的事件发生的时候一律调用该接口来把事件分发到对应的状态节点上去处理。
 * 
 * @author wei.zhang
 * 
 */
public interface EventDispatcher {

	/**
	 * Dispatch the event.
	 * 
	 * @param sessionId
	 *            The session id
	 * @param stateId
	 *            状态基处理流水号
	 * @param eventType
	 *            事件类型
	 * @param parameters
	 *            该事件相关的参数
	 */
	public void dispatch(Long sessionId, Long stateId, EventType eventType,
			Map<String, Serializable> parameters);
}

// $Id$