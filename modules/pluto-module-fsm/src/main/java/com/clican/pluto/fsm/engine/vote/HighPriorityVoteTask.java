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
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;

/**
 * 高低优先级通过类任务投票
 * 
 * @author wei.zhang
 * 
 */
public class HighPriorityVoteTask extends TaskStateImpl {

	
	protected List<Task> assignTasks(State state, Event event) {
		List<Task> taskList = super.assignTasks(state, event);
		Task firstPriorityTask = taskList.get(0);
		taskDao.setVariable(firstPriorityTask, Parameters.FIRST_PRIORITY_ASSIGNEE.getParameter(), true);
		return taskList;
	}

	
	protected void handleTask(Task task, Event event) {
		Calendar current = Calendar.getInstance();
		EventType eventType = EventType.convert(event.getEventType());
		VoteResult voteResult = VoteResult.convert((String) this.getVariableValue(Parameters.VOTE_RESULT.getParameter(), event, false));
		boolean resetLastVoteResult = true;
		if (eventType == EventType.TASK) {
			// 任务触发的事件
			if (log.isDebugEnabled()) {
				log.debug("HighPriorityVoteTask,get a voteResult[" + voteResult + "]");
			}
			Boolean firstPriorityAssignee = (Boolean) this.getVariableValue(Parameters.FIRST_PRIORITY_ASSIGNEE.getParameter(), task, false);
			if (firstPriorityAssignee != null && firstPriorityAssignee) {
				if (log.isDebugEnabled()) {
					log.debug("First priority assignee is voted,voteResult[" + voteResult + "]");
				}
				List<Task> notCompletedTaskList = taskDao.findActiveTasksByState(event.getState());
				for (Task notCompletedTask : notCompletedTaskList) {
					if (notCompletedTask.getId().equals(task.getId())) {
						continue;
					}
					notCompletedTask.setCompleteTime(current.getTime());
					taskDao.setVariable(notCompletedTask, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());
					taskDao.save(notCompletedTask);
				}
			} else {
				VoteResult lastVoteResult = VoteResult.convert((String) this.getVariableValue(Parameters.VOTE_RESULT.getParameter(), event.getState(), false));
				if (lastVoteResult != null) {
					List<Task> completedTaskList = taskDao.findActiveTasksByState(event.getState());
					// 按照任务的数据库id来获得任务的投票的优先级别
					for (Task completedTask : completedTaskList) {
						if (completedTask.getId().compareTo(task.getId()) > 0) {
							resetLastVoteResult = false;
							break;
						}
					}
				}
			}
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), voteResult.getVoteResult());
		} else {
			if (log.isDebugEnabled()) {
				log.debug("HighPriorityVoteTask, get a timeout event");
			}
			// 超时触发的事件
			taskDao.setVariable(task, Parameters.VOTE_RESULT.getParameter(), VoteResult.DISCLAIM.getVoteResult());
			voteResult = VoteResult.DISCLAIM;
		}
		if (resetLastVoteResult) {
			stateDao.setVariable(event.getState(), Parameters.VOTE_RESULT.getParameter(), voteResult.getVoteResult());
			recordLastVoteResultAndSuggestion(task.getState().getSession().getId(), voteResult.getVoteResult(), (String) this.getVariableValue(
					Parameters.AUDIT_SUGGESTION.getParameter(), event, false), task.getAssignee());
		}
		task.setCompleteTime(current.getTime());
		taskDao.save(task);
	}

}

// $Id$