/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import java.util.List;
import java.util.Map;

import com.clican.pluto.fsm.listener.EndListener;
import com.clican.pluto.fsm.listener.StartListener;
import com.clican.pluto.fsm.listener.TimeOutListener;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;

/**
 * 状态处理节点的接口定义类。 主要定义了进/出该状态时候应该触发的Listener， 超时相关的Job以及处理具体的Event的接口
 * 
 * @author wei.zhang
 * 
 */
public interface IState extends EventHandler {

	/**
	 * 当状态切换到该状态基的时候该方法被调用
	 * 
	 * @param session
	 *            当前状态基所对应的<code>Session</code>对象
	 * @param previousState
	 *            上一个状态基对象
	 * @param event
	 *            当前触发状态基变化的<code>Event</code>对象
	 * @return
	 */
	public void onStart(Session session, IState previousState, Event event);

	/**
	 * 当状态从当前状态基离开的时候该方法被调用
	 * 
	 * @param state
	 *            当前的状态基所对应的数据库对象
	 * @param nextState
	 *            下一个状态基对象
	 * @param event
	 *            出发当前状态基转换的<code>Event</code>
	 * @return
	 */
	public void onEnd(State state, List<IState> nextStateList, Event event);

	/**
	 * 设置当前状态基中在<code>onStart</code>方法上被调用的listener.
	 * 
	 * @param startListenerList
	 */
	public void setStartListenerList(List<StartListener> startListenerList);

	/**
	 * 设置当前状态基中在<code>onEnd</code>方法上被调用的listener.
	 * 
	 * @param endListenerList
	 */
	public void setEndListenerList(List<EndListener> endListenerList);

	/**
	 * 设置当前状态基上的一些定时的listener
	 * 
	 * @param timeOutListenerMap
	 */
	public void setTimeOutListenerMap(Map<String, TimeOutListener> timeOutListenerMap);

	/**
	 * 返回在当前状态基上已经设置的listeners
	 * 
	 * @return
	 */
	public Map<String, TimeOutListener> getTimeOutListenerMap();

	/**
	 * 设置当前状态基上的常量
	 * 
	 * @return
	 */
	public void setConstantsMap(Map<String, String> constantsMap);

	/**
	 * 返回在当前状态基上已经设置的常量
	 * 
	 * @return
	 */
	public Map<String, String> getConstantsMap();

	/**
	 * 返回当前状态基的名称
	 * 
	 * @return
	 */
	public String getName();

}

// $Id$