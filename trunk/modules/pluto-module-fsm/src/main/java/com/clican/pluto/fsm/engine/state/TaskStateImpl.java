/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.state;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.listener.TaskListener;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;
import com.clican.pluto.fsm.model.Variable;

/**
 * 任务状态基础。
 * <p>
 * 和DefaultState不同的是该状态基需要一个assignees参数，该参数可以是一个常量也可以是一个变量明，如果是变量名那么就会从各个Scope
 * Context中去查找该变量对应的值。
 * 
 * @author wei.zhang
 * 
 */
public class TaskStateImpl extends DefaultStateImpl {

	private String assignees;

	protected String taskName;

	protected String taskType;

	private List<TaskListener> taskListeners;

	public void setAssignees(String assignees) {
		this.assignees = assignees;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public void setTaskListeners(List<TaskListener> taskListeners) {
		this.taskListeners = taskListeners;
	}

	@Transactional
	public void onStart(Session session, IState previousState, Event event) {
		super.onStart(session, previousState, event);
		State state = this.getLatestState(event.getState().getSession());
		if (Status.convert(state.getStatus()) != Status.ACTIVE) {
			return;
		}
		this.assignTasks(state, event);
	}

	/**
	 * 分配任务给任务完成人
	 * 
	 * @param state
	 * @param event
	 * @return
	 */
	protected List<Task> assignTasks(State state, Event event) {
		List<Task> tasks = new ArrayList<Task>();
		Calendar current = Calendar.getInstance();
		Session session = state.getSession();
		Set<Variable> variableSet = session.getVariableSet();
		Map<String, Object> vars = new HashMap<String, Object>();
		for (Variable var : variableSet) {
			vars.put(var.getName(), var.getValue());
		}
		variableSet = event.getVariableSet();
		for (Variable var : variableSet) {
			vars.put(var.getName(), var.getValue());
		}
		String assigneesValue = (String) MVEL.eval(assignees, vars);
		String[] assignees = assigneesValue.split(",");
		String taskNameValue;
		if (StringUtils.isEmpty(taskName)) {
			taskNameValue = getName();
		} else {
			taskNameValue = (String) MVEL.eval(taskName, vars);
		}
		String taskTypeValue;
		if (StringUtils.isEmpty(taskType)) {
			taskTypeValue = getName();
		} else {
			taskTypeValue = (String) MVEL.eval(taskType, vars);
		}
		for (String assignee : assignees) {
			Task task = newTask(state, assignee.toString(), current.getTime(),
					taskNameValue, taskTypeValue);
			tasks.add(task);
		}
		if (tasks.size() == 0) {
			throw new RuntimeException("There is no task assigned");
		}
		return tasks;
	}

	/**
	 * 创建一个新的任务
	 * 
	 * @param state
	 *            当前任务所属的状态基
	 * @param assignee
	 *            任务的被分配人
	 * @param assignTime
	 *            任务的分配时间
	 * @param taskNameValue
	 *            任务名
	 * @param taskTypeValue
	 *            任务类型
	 * @return
	 */
	protected Task newTask(State state, String assignee, Date assignTime,
			String taskNameValue, String taskTypeValue) {

		Task task = new Task();
		task.setAssignee(assignee);
		task.setAssignTime(assignTime);
		task.setName(taskNameValue);
		task.setType(taskTypeValue);
		task.setState(state);
		task.setEndTime(new Date());
		try {
			if (taskListeners != null) {
				for(TaskListener taskListener:taskListeners){
					taskListener.beforeAssignTask(task);
				}
			}
			taskDao.save(task);
			if (state.getTaskSet() == null) {
				state.setTaskSet(new HashSet<Task>());
			}
			state.getTaskSet().add(task);
			stateDao.save(state);
			return task;
		} finally {
			if (taskListeners != null) {
				for(TaskListener taskListener:taskListeners){
					taskListener.afterAssignTask(task);
				}
			}
		}

	}

	/**
	 * 不同类型的task有不同的实现，但是都要调用taskDao保存一遍task使Event传播来的变量得到保存。
	 * 
	 * @param task
	 * @param event
	 */
	protected void handleTask(Task task, Event event) {
		Calendar current = Calendar.getInstance();
		task.setCompleteTime(current.getTime());
		taskDao.save(task);
	}

	@Transactional
	public void handle(Event event) {
		propagateVariables(event);
		EventType eventType = EventType.convert(event.getEventType());
		if (eventType == EventType.JOB) {
			Set<Task> taskSet = event.getState().getTaskSet();
			for (Task task : taskSet) {
				if (task.getCompleteTime() == null) {
					// 把事件中的变量放入Task中
					setTaskVariable(task, event.getVariableSet());
					try {
						if (taskListeners != null) {
							for(TaskListener taskListener:taskListeners){
								taskListener.beforeHandleTask(task, event);
							}
						}
						handleTask(task, event);
					} finally {
						if (taskListeners != null) {
							for(TaskListener taskListener:taskListeners){
								taskListener.afterHandleTask(task, event);
							}
						}
					}
				}
			}
		} else if (eventType == EventType.TASK) {
			Long taskId = (Long) this.getVariableValue(Parameters.TASK_ID
					.getParameter(), event, false);
			Task task = taskDao.findTaskById(taskId);
			// 把事件中的变量放入Task中
			setTaskVariable(task, event.getVariableSet());
			try {
				if (taskListeners != null) {
					for(TaskListener taskListener:taskListeners){
						taskListener.beforeHandleTask(task, event);
					}
				}
				handleTask(task, event);
			} finally {
				if (taskListeners != null) {
					for(TaskListener taskListener:taskListeners){
						taskListener.afterHandleTask(task, event);
					}
				}
			}
		} else {
			throw new RuntimeException("Unsupported Event [" + event + "]");
		}
		List<Task> notCompletedTask = taskDao.findActiveTasksByState(event
				.getState());
		if (notCompletedTask.size() == 0) {
			onEnd(event);
		}
	}

	private void setTaskVariable(Task task, Set<Variable> varialbleSet) {
		for (Variable va : varialbleSet) {
			taskDao.setVariable(task, va.getName(), va.getValue());
		}
	}

	protected void recordLastVoteResultAndSuggestion(Long sessionId,
			String voteResult, String suggestion, String lastAuditor) {
		Session session = sessionDao.findSessionById(sessionId);
		sessionDao.setVariable(session, Parameters.VOTE_RESULT.getParameter(),
				voteResult);
		sessionDao.setVariable(session, Parameters.AUDIT_SUGGESTION
				.getParameter(), suggestion);
		sessionDao.setVariable(session, "lastAuditor", lastAuditor);
	}

}

// $Id$