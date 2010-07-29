/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.state;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mvel2.MVEL;

import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.listener.TimeOutListener;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Variable;

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

	private Map<String, Object> conditionNextStateMap;

	public void setConditionNextStateMap(
			Map<String, Object> conditionNextStateMap) {
		this.conditionNextStateMap = conditionNextStateMap;
	}

	
	public void setTimeOutListenerMap(
			Map<String, TimeOutListener> timeOutListenerMap) {
		throw new RuntimeException(
				"The DecisitonStateImpl doesn't support TimeOutListener");
	}

	@SuppressWarnings("unchecked")
	
	public void onStart(Session session, IState previousState, Event event) {
		super.onStart(session, previousState, event);
		State state = this.getLatestState(session);
		if (Status.convert(state.getStatus()) != Status.ACTIVE) {
			return;
		}
		session.setLastUpdateTime(new Date());
		sessionDao.save(session);
		Set<Variable> variableSet = session.getVariableSet();
		Map<String, Object> vars = new HashMap<String, Object>();
		for (Variable var : variableSet) {
			vars.put(var.getName(), var.getValue());
		}
		variableSet = event.getVariableSet();
		for (Variable var : variableSet) {
			vars.put(var.getName(), var.getValue());
		}
		
		variableSet = event.getState().getVariableSet();
		for (Variable var : variableSet) {
			vars.put(var.getName(), var.getValue());
		}

		List<IState> nextStateList = new ArrayList<IState>();
		for (String expression : conditionNextStateMap.keySet()) {
			if (MVEL.evalToBoolean(expression, vars)) {
				Object obj = conditionNextStateMap.get(expression);
				if (obj instanceof IState) {
					nextStateList.add((IState) obj);
				} else if (obj instanceof String) {
					String nextStateName = (String) this.getVariableValueByEL(
							(String) obj, session);
					String[] nextStateNames = nextStateName.split(",");
					if (nextStateNames.length == 1) {
						nextStateList.add(engineContext
								.getState(session.getName(), session
										.getVersion(), nextStateName));
					} else {
						for (int i = 0; i < nextStateNames.length; i++) {
							String temp = nextStateNames[i];
							nextStateList.add(engineContext.getState(session
									.getName(), session.getVersion(), temp));
						}
					}
				} else if (obj instanceof List) {
					nextStateList.addAll((List<IState>) obj);
				}
				onEnd(state, nextStateList, event);
				return;
			}
		}
		throw new RuntimeException("There is no match condition");
	}

	
	public void handle(Event event) {
		throw new RuntimeException("The decision state can't handler event");
	}
}

// $Id$