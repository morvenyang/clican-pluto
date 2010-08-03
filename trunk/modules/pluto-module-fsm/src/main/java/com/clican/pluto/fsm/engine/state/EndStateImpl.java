/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.state;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;

/**
 * 工作流结束状态基，一旦工作流进入该状态基就表明整个Session的结束。
 * 
 * @author wei.zhang
 * 
 */
public class EndStateImpl extends DefaultStateImpl {

	@Transactional
	public void onEnd(Event event) {
		super.onEnd(event);
		State state = event.getState();
		Session session = state.getSession();
		session.setStatus(Status.INACTIVE.getStatus());
		session.setEndTime(new Date());
		sessionDao.save(session);
		jobDao.deleteJobsBySessionId(session.getId());
		stateDao.inactiveStatesBySessionId(session.getId());
		taskDao.completeTasksBySessionId(session.getId());
	}

	public void onStart(IState previousState, Event event) {
		super.onStart(previousState, event);
		Session session = event.getState().getSession();
		State state = getLatestState(session);
		if (Status.convert(state.getStatus()) != Status.ACTIVE) {
			return;
		}
		onEnd(event);
	}

}

// $Id$