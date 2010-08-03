/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.VoteResult;
import com.clican.pluto.fsm.model.Session;

public class ReportAuditWorkFlowTestCase extends BaseFsmEngineTestCase {

	public void testHighWorkFlow() throws Exception {
		Session session = engineContext.newSession("reportAudit", "18");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("auditLevel", "high");
		parameters.put("riskAuditor", "12");

		eventDispatcher.dispatch(session.getId(), engineContext.getActiveState(
				session.getId()).get(0).getId(), EventType.NORMAL, parameters);
		validateState(session, "riskAuditing");
		this.simulateCompleteTask(session, "12", "riskAuditing",
				VoteResult.DISAGREE);
		validateState(session, "riskModify");
		this.simulateCompleteTask(session, "18", "riskModify", null);
		validateState(session, "riskAuditing");
		this.simulateCompleteTask(session, "12", "riskAuditing",
				VoteResult.AGREE);
		validateSessionEnd(session);
	}

	public void testNormalWorkFlow() throws Exception {
		Session session = engineContext.newSession("reportAudit", "18");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("auditLevel", "normal");
		parameters.put("riskAuditor", "12");
		parameters.put("chiefAuditor", "13");
		parameters.put("teacherAuditor", "14");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL,
				parameters);
		validateState(session, "chiefAuditing");
		this.simulateCompleteTask(session, "13", "chiefAuditing",
				VoteResult.DISAGREE);
		validateState(session, "chiefModify");
		this.simulateCompleteTask(session, "18", "chiefModify", null);
		validateState(session, "chiefAuditing");
		this.simulateCompleteTask(session, "13", "chiefAuditing",
				VoteResult.AGREE);
		validateState(session, "riskAuditing");
		this.simulateCompleteTask(session, "12", "riskAuditing",
				VoteResult.AGREE);
		validateSessionEnd(session);
	}

	public void testNormalWorkFlow2() throws Exception {
		Session session = engineContext.newSession("reportAudit", "18");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("auditLevel", "normal");
		parameters.put("riskAuditor", "12");
		parameters.put("chiefAuditor", "13");
		parameters.put("leaders", "1,2,3");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL,
				parameters);
		validateState(session, "chiefAuditing");
		this.simulateCompleteTask(session, "13", "chiefAuditing",
				VoteResult.DISCLAIM);
		validateState(session, "groupAuditing");
		this.simulateCompleteTask(session, "1", "groupAuditing",
				VoteResult.AGREE);
		validateState(session, "groupAuditing");
		this.simulateCompleteTask(session, "2", "groupAuditing",
				VoteResult.DISAGREE);
		validateState(session, "groupAuditing");
		this.simulateCompleteTask(session, "3", "groupAuditing",
				VoteResult.DISAGREE);
		validateState(session, "chiefModify");
		this.simulateCompleteTask(session, "18", "chiefModify", null);
		validateState(session, "chiefAuditing");
		this.simulateCompleteTask(session, "13", "chiefAuditing",
				VoteResult.DISCLAIM);
		this.simulateCompleteTask(session, "1", "groupAuditing",
				VoteResult.AGREE);
		validateState(session, "groupAuditing");
		this.simulateCompleteTask(session, "2", "groupAuditing",
				VoteResult.AGREE);
		validateState(session, "riskAuditing");
		this.simulateCompleteTask(session, "12", "riskAuditing",
				VoteResult.AGREE);
		validateSessionEnd(session);
	}

	public void testLowWorkFlow() throws Exception {
		Session session = engineContext.newSession("reportAudit", "18");
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("auditLevel", "low");
		parameters.put("chiefAuditor", "13");
		parameters.put("teacherAuditor", "14");
		eventDispatcher.dispatch(session.getId(), this.engineContext.getActiveState(session.getId()).get(0).getId(), EventType.NORMAL,
				parameters);
		validateState(session, "teacherAuditing");
		this.simulateCompleteTask(session, "14", "teacherAuditing",
				VoteResult.DISAGREE);
		validateState(session, "teacherModify");
		this.simulateCompleteTask(session, "18", "teacherModify", null);
		validateState(session, "teacherAuditing");
		this.simulateCompleteTask(session, "14", "teacherAuditing",
				VoteResult.AGREE);
		validateState(session, "chiefAuditing");
		this.engineContext.deleteSession(session.getId());
	}
}

// $Id$