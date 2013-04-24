/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.state;

import java.util.Date;

import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;

/**
 * 决策状态基。
 * 
 * 该状态基础不同于DefaultState的就是它不会使用nextState对象，而是使用一个conditionNextStateMap，
 * 然后根据运行时的变量来决定下一个状态基是什么。
 * 
 * @author wei.zhang
 * 
 */
public class DecisionStateImpl extends DefaultStateImpl {

	public void onStart(Session session, IState previousState, Event event) {
		super.onStart(session, previousState, event);
		State state = this.getLatestState(session);
		if (Status.convert(state.getStatus()) != Status.ACTIVE) {
			return;
		}
		session.setLastUpdateTime(new Date());
		sessionDao.save(session);
		super.onEnd(event);
	}

	public void handle(Event event) {
		throw new RuntimeException("The decision state can't handler event");
	}
}

// $Id$