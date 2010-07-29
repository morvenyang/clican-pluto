/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.jsw.fsm.engine;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.VoteResult;
import com.clican.pluto.fsm.model.Session;

public class TravelWorkFlowTestCase extends BaseFsmEngineTestCase {

	public void testNormalWorkFlow() throws Exception {
		Session session = engineContext.newSession("travel", "4");
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_MONTH, 1);
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 1);
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("leaders", "1,2,3");
		parameters.put("startDate", startDate.getTime());
		parameters.put("endDate", startDate.getTime());
		parameters.put("tripType", "notsurvey");
		eventDispatcher.dispatch(session.getId(), this.engineContext
				.getActiveState(session.getId()).get(0).getId(),
				EventType.NORMAL, parameters);
		this.simulateCompleteTask(session, "1", "auditing_application",
				VoteResult.AGREE);
		validateState(session, "application_pass");
		this.executeCurrentJobs(session);
		validateState(session, "on_travel");
		this.executeCurrentJobs(session);
		this.simulateCompleteTask(session, "4", "submit_report", null,
				parameters);
		this.simulateCompleteTask(session, "1", "auditing_report",
				VoteResult.AGREE);
		validateState(session, "print_expense");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL,
				new HashMap<String, Serializable>());
		validateSessionEnd(session);
	}

	public void testTimeoutWorkFlow() throws Exception {
		Session session = engineContext.newSession("travel", "4");
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.MINUTE, 1);
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MINUTE, 4);
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("leaders", "1,2,3");
		parameters.put("startDate", startDate.getTime());
		parameters.put("endDate", endDate.getTime());
		parameters.put("tripType", "notsurvey");
		eventDispatcher.dispatch(session.getId(), this.engineContext
				.getActiveState(session.getId()).get(0).getId(),
				EventType.NORMAL, parameters);
		this.simulateCompleteTask(session, "1", "auditing_application",
				VoteResult.AGREE);
		validateState(session, "application_pass");
		Thread.sleep(2 * 60 * 1000);
		validateState(session, "on_travel");
		Thread.sleep(2 * 60 * 1000);
		this.simulateCompleteTask(session, "4", "submit_report", null,
				parameters);
		this.simulateCompleteTask(session, "1", "auditing_report",
				VoteResult.AGREE);
		validateState(session, "print_expense");
		eventDispatcher.dispatch(session.getId(), this.engineContext
				.getActiveState(session.getId()).get(0).getId(),
				EventType.NORMAL, new HashMap<String, Serializable>());
		validateSessionEnd(session);
	}
}

// $Id$