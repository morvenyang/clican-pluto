/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.Mockery;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.clican.pluto.fsm.engine.EngineContext;
import com.clican.pluto.fsm.engine.EventDispatcher;
import com.clican.pluto.fsm.engine.JobContext;
import com.clican.pluto.fsm.engine.JobService;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.enumeration.VoteResult;
import com.clican.pluto.fsm.model.Job;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;

public abstract class BaseFsmEngineTestCase extends
		AbstractDependencyInjectionSpringContextTests {

	protected Log log = LogFactory.getLog(this.getClass());

	protected Mockery context = new Mockery();

	protected EngineContext engineContext;

	protected EventDispatcher eventDispatcher;

	protected JobService jobService;

	protected JobContext jobContext;

	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:META-INF/spring-fsm-test-*.xml" };
	}

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

	public void setJobContext(JobContext jobContext) {
		this.jobContext = jobContext;
	}

	protected void executeCurrentJobs(Session session) {
		List<State> activeStates = engineContext.getActiveState(session.getId());
		for(State activeState:activeStates){
			List<Job> idelJobs = this.jobService.findIdelJobsByState(activeState);
			for (Job job : idelJobs) {
				jobContext.executeJob(job);
			}
		}
	}

	protected void simulateCompleteTask(Session session, String userId,
			String stateName, VoteResult voteResult) {
		this.simulateCompleteTask(session, userId, stateName, voteResult,
				new HashMap<String, Serializable>());
	}

	protected void simulateCompleteTask(Session session, String userId,
			String stateName, VoteResult voteResult,
			Map<String, Serializable> parameters) {
		List<Task> tasks = engineContext.queryTask(userId, session.getName(),
				stateName, false);
		Map<String, Serializable> params = new HashMap<String, Serializable>(
				parameters);
		// 一般来说只有一个任务
		Long stateId = null;
		for (Task task : tasks) {
			if (voteResult != null) {
				params.put(Parameters.VOTE_RESULT.getParameter(), voteResult
						.getVoteResult());
			}
			stateId = task.getState().getId();
			params.put(Parameters.TASK_ID.getParameter(), task.getId());
			break;
		}
		eventDispatcher.dispatch(session.getId(), stateId, EventType.TASK,
				params);
	}

	protected void validateState(Session session, String stateName) {
		List<State> states = engineContext.getActiveState(session.getId());
		boolean hasState = false;
		for(State state:states){
			if(state.getName().equals(stateName)){
				hasState = true;
				break;
			}
		}
		assertTrue(hasState);
	}

	protected void validateSessionEnd(Session session) {
		session = engineContext.querySession(session.getId());
		assertEquals(Status.INACTIVE, Status.convert(session.getStatus()));
	}
}

// $Id$