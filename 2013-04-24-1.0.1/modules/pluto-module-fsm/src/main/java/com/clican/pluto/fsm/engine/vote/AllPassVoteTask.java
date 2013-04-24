/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.vote;

import java.util.Calendar;
import java.util.List;

import com.clican.pluto.fsm.engine.state.TaskStateImpl;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.enumeration.VoteResult;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Task;

/**
 * 全部通过的投票类任务
 * 
 * @author wei.zhang
 * 
 */
public class AllPassVoteTask extends TaskStateImpl {

	
	protected void handleTask(Task task, Event event) {
		if (log.isDebugEnabled()) {
			log.debug("AllPassVoteTask,state[" + this.getName() + "] handle event");
		}
		Calendar current = Calendar.getInstance();
		EventType eventType = EventType.convert(event.getEventType());

		VoteResult voteResult = VoteResult.convert((String) this.getVariableValue(Parameters.VOTE_RESULT.getParameter(), event, false));
		if (eventType == EventType.TASK) {
			// 任务触发的事件
			if (log.isDebugEnabled()) {
				log.debug("AllPassVoteTask,get a voteResult[" + voteResult + "]");
			}
			if (voteResult != VoteResult.AGREE) {
				List<Task> notCompletedTaskList = taskDao.findActiveTasksByState(event.getState());
				for (Task notCompletedTask : notCompletedTaskList) {
					if (notCompletedTask.getId().equals(task.getId())) {
						continue;
					}
					notCompletedTask.setCompleteTime(current.getTime());
					taskDao.setVariable(notCompletedTask, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());
					taskDao.save(notCompletedTask);
				}
			}
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), voteResult.getVoteResult());
		} else {
			// 超时触发的事件
			if (log.isDebugEnabled()) {
				log.debug("AllPassVoteTask,get a timeout event");
			}
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());
			voteResult = VoteResult.DISCLAIM;
		}
		stateDao.setVariable(event.getState(), Parameters.VOTE_RESULT.getParameter(), voteResult.getVoteResult());
		recordLastVoteResultAndSuggestion(task.getState().getSession().getId(), voteResult.getVoteResult(), (String) this.getVariableValue(
				Parameters.AUDIT_SUGGESTION.getParameter(), event, false), task.getAssignee());
		task.setCompleteTime(current.getTime());
		taskDao.save(task);
	}

}

// $Id$