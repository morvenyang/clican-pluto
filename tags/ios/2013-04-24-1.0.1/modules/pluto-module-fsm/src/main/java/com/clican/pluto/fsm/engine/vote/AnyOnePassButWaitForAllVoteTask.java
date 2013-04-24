/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.vote;

import java.util.Calendar;
import java.util.Set;

import com.clican.pluto.fsm.engine.state.TaskStateImpl;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.enumeration.VoteResult;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;
import com.clican.pluto.fsm.model.Variable;

/**
 * 
 * 任务投票:一人同意则整体通过；但需要所有人都投票之后才统计结果
 * 
 * @author xiaoming.lu
 * 
 */
public class AnyOnePassButWaitForAllVoteTask extends TaskStateImpl {

	
	protected void handleTask(Task task, Event event) {
		if (log.isDebugEnabled()) {
			log.debug("AnyOnePassButWaitForAllVoteTask,state[" + this.getName() + "] handle event");
		}
		Calendar current = Calendar.getInstance();
		EventType eventType = EventType.convert(event.getEventType());
		VoteResult voteResult = VoteResult.convert((String) this.getVariableValue(Parameters.VOTE_RESULT.getParameter(), event, false));
		if (eventType == EventType.TASK) {
			// 任务触发的事件
			if (log.isDebugEnabled()) {
				log.debug("AllPassVoteTask,get a voteResult[" + voteResult + "]");
			}
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), voteResult.getVoteResult());
			// 只要有一人同意，则通过
		} else {
			// 超时触发的事件
			if (log.isDebugEnabled()) {
				log.debug("AnyOnePassButWaitForAllVoteTask,get a timeout event");
			}
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());

			// 如果判断是否已经有人同意，否则为超时
			State state = event.getState();
			boolean agreed = false;
			Set<Variable> variables = state.getVariableSet();
			for (Variable var : variables) {
				if (var.getName().equals(Parameters.VOTE_RESULT.getParameter())) {
					agreed = VoteResult.AGREE.getVoteResult().equals(var.getValue());
					break;
				}
			}
			if (agreed) {
				voteResult = VoteResult.AGREE;
			} else {
				voteResult = VoteResult.DISCLAIM;
			}
		}
		stateDao.setVariable(event.getState(), Parameters.VOTE_RESULT.getParameter(), voteResult.getVoteResult());
		recordLastVoteResultAndSuggestion(task.getState().getSession().getId(), voteResult.getVoteResult(), (String) this.getVariableValue(
				Parameters.AUDIT_SUGGESTION.getParameter(), event, false), task.getAssignee());
		task.setCompleteTime(current.getTime());
		taskDao.save(task);
	}
}

// $Id$