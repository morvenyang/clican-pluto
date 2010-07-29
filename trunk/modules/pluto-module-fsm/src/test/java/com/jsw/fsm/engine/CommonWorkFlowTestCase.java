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

public class CommonWorkFlowTestCase extends BaseFsmEngineTestCase {

	public void testWorkFlow_1() throws Exception {
		Session session = engineContext.newSession("workflow", "4");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("auditors", "1,2,3");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL, parameters);
		this.simulateCompleteTask(session, "1", "audit", VoteResult.AGREE);
		validateState(session, "audit");
		this.simulateCompleteTask(session, "2", "audit", VoteResult.AGREE);
		validateState(session, "audit");
		this.simulateCompleteTask(session, "3", "audit", VoteResult.AGREE);
		validateSessionEnd(session);
	}

	public void testWorkFlow_2() throws Exception {
		Session session = engineContext.newSession("workflow", "4");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("auditors", "1,2,3");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL, parameters);
		this.simulateCompleteTask(session, "1", "audit", VoteResult.DISAGREE);
		validateState(session, "audit");
		this.simulateCompleteTask(session, "2", "audit", VoteResult.DISAGREE);
		validateState(session, "audit");
		this.simulateCompleteTask(session, "3", "audit", VoteResult.AGREE);
		validateSessionEnd(session);
	}

	public void testWorkFlow_3() throws Exception {
		Session session = engineContext.newSession("workflow", "4");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("auditors", "1,2,3");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL, parameters);
		this.simulateCompleteTask(session, "1", "audit", VoteResult.DISAGREE);
		validateState(session, "audit");
		this.simulateCompleteTask(session, "2", "audit", VoteResult.DISAGREE);
		validateState(session, "audit");
		this.simulateCompleteTask(session, "3", "audit", VoteResult.DISAGREE);
		validateState(session, "audit");
	}

}

// $Id$