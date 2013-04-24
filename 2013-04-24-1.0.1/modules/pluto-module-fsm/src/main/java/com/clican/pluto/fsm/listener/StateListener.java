/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener;

import java.util.List;

import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.State;

public interface StateListener {

	/**
	 * 当状态基开始时该listener被调用
	 * @param state
	 * @param previousState
	 * @param event
	 */
	public void onStart(State state, IState previousState,Event event);
	
	/**
	 * 当状态基结束时该listener被调用
	 * @param state
	 * @param nextState
	 * @param event
	 */
	public void onEnd(State state, List<IState> nextStateList, Event event);
}

// $Id$