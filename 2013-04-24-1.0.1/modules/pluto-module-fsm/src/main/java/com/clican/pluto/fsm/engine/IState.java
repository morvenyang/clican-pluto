/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import java.util.List;
import java.util.Map;

import com.clican.pluto.fsm.listener.StateListener;
import com.clican.pluto.fsm.listener.TimeOutListener;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Session;

/**
 * 状态处理节点的接口定义类。 主要定义了进/出该状态时候应该触发的Listener， 超时相关的Job以及处理具体的Event的接口
 * 
 * @author wei.zhang
 * 
 */
public interface IState extends EventHandler {

	/**
	 * 当状态切换到该状态基的时候该方法被调用
	 * @param session TODO
	 * @param previousState
	 *            上一个状态基对象
	 * @param event
	 *            当前触发状态基变化的<code>Event</code>对象
	 * @return
	 */
	public void onStart(Session session, IState previousState, Event event);

	/**
	 * 当状态从当前状态基离开的时候该方法被调用
	 * @param event
	 *            出发当前状态基转换的<code>Event</code>
	 * @param nextState
	 *            下一个状态基对象
	 * 
	 * @return
	 */
	public void onEnd(Event event);

	public List<StateListener> getStateListeners();

	public void setStateListeners(List<StateListener> startListeners);

	public Map<String, TimeOutListener> getTimeoutListeners() ;

	public void setTimeoutListeners(Map<String, TimeOutListener> timeoutListeners);

	/**
	 * 设置当前状态基上的常量
	 * 
	 * @return
	 */
	public void setParams(Map<String, String> constantsMap);

	/**
	 * 返回在当前状态基上已经设置的常量
	 * 
	 * @return
	 */
	public Map<String, String> getParams();

	/**
	 * 返回当前状态基的名称
	 * 
	 * @return
	 */
	public String getName();

}

// $Id$