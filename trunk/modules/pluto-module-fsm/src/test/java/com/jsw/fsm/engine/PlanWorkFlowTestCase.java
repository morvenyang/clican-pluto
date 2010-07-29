/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.jsw.fsm.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.VoteResult;
import com.clican.pluto.fsm.model.Session;

public class PlanWorkFlowTestCase extends BaseFsmEngineTestCase {

	public void testNormalWorkFlow() throws Exception {
		final Session session = engineContext.newSession("plan", "4");
		final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("leaders", "1,2,3");
		new Thread(){

			@Override
			public void run() {
				eventDispatcher.dispatch(session.getId(), engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL,
						parameters);
			}
			
		}.start();
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL,
				parameters);
		this.simulateCompleteTask(session, "1", "auditing", VoteResult.AGREE);
		this.validateSessionEnd(session);
	}

	public void testReviseWorkFlow() throws Exception {
		Session session = engineContext.newSession("plan", "4");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("leaders", "1,2,3");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL,
				parameters);
		simulateCompleteTask(session, "1", "auditing", VoteResult.DISAGREE);
		validateState(session, "revise");
		simulateCompleteTask(session, "4", "revise", null, parameters);
		validateState(session, "auditing");
		simulateCompleteTask(session, "2", "auditing", VoteResult.DISAGREE);
		simulateCompleteTask(session, "3", "auditing", VoteResult.DISAGREE);
		simulateCompleteTask(session, "1", "auditing", VoteResult.AGREE);
		validateSessionEnd(session);
	}
}

// $Id$