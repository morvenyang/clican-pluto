/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.vote;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.fsm.engine.state.TaskStateImpl;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.enumeration.VoteResult;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Task;

/**
 * 
 * 百分比通过类任务投票
 * 
 * @author wei.zhang
 * 
 */
public class PercentVoteTask extends TaskStateImpl {

	protected void handleTask(Task task, Event event) {
		double percent = 0.5d;
		if (StringUtils.isNotEmpty(params.get("percent"))) {
			percent = Double.parseDouble(params.get("percent"));
		}
		Calendar current = Calendar.getInstance();
		EventType eventType = EventType.convert(event.getEventType());
		float totalTasks = event.getState().getTaskSet().size();
		Integer votedAgreeNumber = (Integer) this.getVariableValue(Parameters.VOTED_AGREE_NUMBER.getParameter(), event.getState(), false);
		if (votedAgreeNumber == null) {
			votedAgreeNumber = 0;
		}
		VoteResult voteResult = null;
		if (eventType == EventType.TASK) {
			voteResult = VoteResult.convert((String) this.getVariableValue(Parameters.VOTE_RESULT.getParameter(), event, false));
			if (log.isDebugEnabled()) {
				log.debug("PercentVoteTask, get a voteResult[" + voteResult + "]");
			}
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), voteResult.getVoteResult());
		} else {
			if (log.isDebugEnabled()) {
				log.debug("PercentVoteTask, get a timeout event");
			}
			voteResult = VoteResult.DISCLAIM;
			// 超时触发的事件
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());
		}
		if (voteResult == VoteResult.AGREE) {
			votedAgreeNumber++;
			stateDao.setVariable(event.getState(), Parameters.VOTED_AGREE_NUMBER.getParameter(), votedAgreeNumber);
		}
		List<Task> notCompletedTaskList = taskDao.findActiveTasksByState(event.getState());
		if (votedAgreeNumber / totalTasks >= percent) {
			voteResult = VoteResult.AGREE;
			for (Task notCompletedTask : notCompletedTaskList) {
				if (notCompletedTask.getId().equals(task.getId())) {
					continue;
				}
				notCompletedTask.setCompleteTime(current.getTime());
				taskDao.setVariable(notCompletedTask, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());
				taskDao.save(notCompletedTask);
			}
		} else if ((votedAgreeNumber + notCompletedTaskList.size() - 1) / totalTasks < percent) {
			voteResult = VoteResult.DISAGREE;
			for (Task notCompletedTask : notCompletedTaskList) {
				if (notCompletedTask.getId().equals(task.getId())) {
					continue;
				}
				notCompletedTask.setCompleteTime(current.getTime());
				taskDao.setVariable(notCompletedTask, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());
				taskDao.save(notCompletedTask);
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