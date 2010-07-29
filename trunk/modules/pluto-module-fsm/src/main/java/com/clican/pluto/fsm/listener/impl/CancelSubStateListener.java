/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.listener.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.fsm.dao.JobDao;
import com.clican.pluto.fsm.dao.SessionDao;
import com.clican.pluto.fsm.dao.StateDao;
import com.clican.pluto.fsm.dao.TaskDao;
import com.clican.pluto.fsm.engine.EngineContext;
import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.listener.EndListener;
import com.clican.pluto.fsm.listener.StartListener;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.State;

/**
 * 用来当有2个或多个子流程的时候，如果其中某个子流程回到了原先的父流程，则强制结束其相关的兄弟流程。
 * 
 * @author wei.zhang
 * 
 */
public class CancelSubStateListener implements StartListener, EndListener {

	private final static Log log = LogFactory
			.getLog(CancelSubStateListener.class);

	private String[] subStateNames;

	private StateDao stateDao;

	private SessionDao sessionDao;

	private EngineContext engineContext;

	private JobDao jobDao;

	private TaskDao taskDao;

	public void setSubStateNames(String[] subStateNames) {
		this.subStateNames = subStateNames;
	}

	public void setStateDao(StateDao stateDao) {
		this.stateDao = stateDao;
	}

	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	
	public void onStart(State state, IState previousState, Event event) {
		cancelSubState(state.getSession().getId());
	}

	
	public void onEnd(State state, List<IState> nextStateList, Event event) {
		cancelSubState(state.getSession().getId());
	}

	/**
	 * 结束所有罗列的兄弟流程节点
	 * 
	 * @param sessionId
	 */
	private void cancelSubState(Long sessionId) {
		if (subStateNames == null || subStateNames.length == 0) {
			log.warn("The suStateNames is null");
			return;
		}
		String canceledStateNames = "";
		List<State> states = engineContext.getActiveAndPendingState(sessionId);
		for (State state : states) {
			for (int i = 0; i < subStateNames.length; i++) {
				if (subStateNames[i].equals(state.getName())) {
					canceledStateNames += subStateNames[i] + ",";
					state.setStatus(Status.INACTIVE.getStatus());
					stateDao.save(state);
					jobDao.deleteJobsByStateId(state.getId());
					taskDao.completeTasksByStateId(state.getId());
				}
			}
		}
		if (canceledStateNames.endsWith(",")) {
			canceledStateNames = canceledStateNames.substring(0,
					canceledStateNames.length() - 1);
		}
		sessionDao.setVariable(sessionDao.findSessionById(sessionId),
				Parameters.CANCELED_STATE_NAMES.getParameter(),
				canceledStateNames);
	}

}

// $Id$