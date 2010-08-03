/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.state;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.MVEL;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.common.calendar.BusinessCalendar;
import com.clican.pluto.fsm.dao.EventDao;
import com.clican.pluto.fsm.dao.JobDao;
import com.clican.pluto.fsm.dao.SessionDao;
import com.clican.pluto.fsm.dao.StateDao;
import com.clican.pluto.fsm.dao.TaskDao;
import com.clican.pluto.fsm.engine.EngineContext;
import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.engine.JobContext;
import com.clican.pluto.fsm.enumeration.Propagation;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.listener.EndListener;
import com.clican.pluto.fsm.listener.StartListener;
import com.clican.pluto.fsm.listener.TimeOutListener;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Job;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;
import com.clican.pluto.fsm.model.Variable;

/**
 * 默认状态基的实现。
 * 
 * 当状态基开始时触发所有的startListener
 * <p>
 * 当状态基结束的时候触发所有的endListener
 * <p>
 * 当状态基开始的时候启动所有的定时任务
 * 
 * @author wei.zhang
 * 
 */
public class DefaultStateImpl implements IState {

	protected final Log log = LogFactory.getLog(getClass());

	protected StateDao stateDao;

	protected SessionDao sessionDao;

	protected EventDao eventDao;

	protected TaskDao taskDao;

	protected JobDao jobDao;

	protected JobContext jobContext;

	protected String name;

	protected Integer value;

	protected List<IState> nextStates;

	protected List<StartListener> startListeners;

	protected List<EndListener> endListeners;

	protected Map<String, TimeOutListener> timeoutListeners;

	protected Map<String, String> params;

	protected BusinessCalendar businessCalendar;

	protected Propagation propagation;

	protected int previousStateNumber = 1;

	protected EngineContext engineContext;

	protected Map<String, List<IState>> nextCondStates;

	public void setPropagation(String propagation) {
		this.propagation = Propagation.convert(propagation);
	}

	public void setNextStates(List<IState> nextStates) {
		this.nextStates = nextStates;
	}

	public void setPreviousStateNumber(int previousStateNumber) {
		this.previousStateNumber = previousStateNumber;
	}

	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public void setNextCondStates(Map<String, List<IState>> nextCondStates) {
		this.nextCondStates = nextCondStates;
	}

	@Transactional
	public void onEnd(Event event) {
		State state = event.getState();
		List<IState> nextStateList = null;
		if (this.nextStates != null && this.nextStates.size() != 0) {
			nextStateList = nextStates;
		} else if (this.nextCondStates != null && this.nextCondStates.size() != 0) {
			for (String expr : this.nextCondStates.keySet()) {
				Boolean result = (Boolean) this.getVariableValueByEL(expr, event, true);
				if (result) {
					nextStateList = this.nextCondStates.get(expr);
				}
			}
			if (nextStateList == null) {
				throw new RuntimeException("There is no match condition");
			}
		}
		if (endListeners != null) {
			for (EndListener listener : endListeners) {
				if (log.isDebugEnabled()) {
					log.debug("execute state listener[" + listener.getClass().getName() + "]");
				}
				listener.onEnd(state, nextStateList, event);
			}
		}
		Session session = state.getSession();
		state.setStatus(Status.INACTIVE.getStatus());
		state.setEndTime(new Date());
		session.setLastUpdateTime(new Date());
		jobDao.deleteJobsByStateId(state.getId());
		taskDao.completeTasksByStateId(state.getId());
		stateDao.save(state);
		sessionDao.save(session);
		if (nextStateList != null) {
			for (IState istate : nextStateList) {
				istate.onStart(this, event);
			}
		}
	}

	@Transactional
	public void onStart(IState previousState, Event event) {
		State state;
		Session session = event.getState().getSession();
		if (previousStateNumber != 1) {
			state = stateDao.getPendingState(session.getId(), this.getName());
			if (state == null) {
				state = new State();
				state.setValue(value);
				state.setStatus(Status.PENDING.getStatus());
				state.setSession(session);
				state.setName(name);
				state.setJobSet(new HashSet<Job>());
				prepareStateConstants(state);
				stateDao.save(state);
				session.getStateSet().add(state);
				sessionDao.save(session);
			} else {
				state.setPreviousStateNumber(state.getPreviousStateNumber() + 1);
				if (state.getPreviousStateNumber() == previousStateNumber) {
					state.setStatus(Status.ACTIVE.getStatus());
					state.setStartTime(new Date());
				}
				stateDao.save(state);
			}
		} else {
			state = new State();
			state.setValue(value);
			state.setStatus(Status.ACTIVE.getStatus());
			state.setSession(session);
			state.setName(name);
			state.setJobSet(new HashSet<Job>());
			state.setStartTime(new Date());
			prepareStateConstants(state);
			stateDao.save(state);
			session.getStateSet().add(state);
			sessionDao.save(session);
		}
		if (Status.convert(state.getStatus()) != Status.ACTIVE) {
			if (log.isDebugEnabled()) {
				log.debug("state=[" + this.getName() + "] is pending and wait for other substate to be end.");
			}
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("state=[" + this.getName() + "] is started");
		}
		if (startListeners != null) {
			for (StartListener listener : startListeners) {
				if (log.isDebugEnabled()) {
					log.debug("execute state listener[" + listener.getClass().getName() + "]");
				}
				listener.onStart(state, previousState, event);
			}
		}
		prepareJobs(state, event, null);

	}

	/**
	 * 将流程定义配置
	 * 
	 * @param state
	 */
	private void prepareStateConstants(State state) {
		if (params == null || params.isEmpty()) {
			return;
		}
		Set<Variable> variableSet = state.getVariableSet();
		if (variableSet == null) {
			variableSet = new HashSet<Variable>();
			state.setVariableSet(variableSet);
		}
		for(String key:params.keySet()){
			Variable var = new Variable();
			var.setState(state);
			var.setName(key);
			var.setValue(params.get(key));
			var.setChangeDate(new Date());
			variableSet.add(var);
		}
	}

	/**
	 * 根据流程文件中配置的timeOutListenner来生成相应的Job
	 * 
	 * @param state
	 * @param event
	 * @param startTime
	 *            重新安排的job执行时间
	 */
	protected void prepareJobs(State state, Event event, Date startTime) {
		if (timeoutListeners != null && !timeoutListeners.isEmpty()) {
			for (String name : timeoutListeners.keySet()) {
				if (log.isDebugEnabled()) {
					log.debug("prepare timeout listener[" + name + "] of state[" + state.getName() + "].");
				}
				TimeOutListener listener = timeoutListeners.get(name);
				BusinessCalendar businessCalendar = listener.getBusinessCalendar() != null ? listener.getBusinessCalendar() : this.businessCalendar;
				Calendar temp = Calendar.getInstance();
				if (StringUtils.isNotEmpty(listener.getStartTime())) {
					Serializable startTimeSer = getVariableValueByEL(listener.getStartTime(), event, true);
					if (startTimeSer instanceof Date) {
						temp.setTime((Date) startTimeSer);
					} else if (startTimeSer instanceof Calendar) {
						temp.setTime(((Calendar) startTimeSer).getTime());
					} else {
						if (StringUtils.isNumeric(startTimeSer.toString())) {
							temp.setTimeInMillis(Long.parseLong(startTimeSer.toString()));
						}
					}
				}
				Job job = new Job();
				job.setName(name);
				job.setBusinessCalendarName(listener.getBusinessCalendarName());
				Serializable dueTime = this.getVariableValueByEL(listener.getDueTime(), event, true);
				if (dueTime == null) {
					// 如果此Job没有设置开始时间,且没有开始时间 就不创建这个Job
					if (startTime == null) {
						log.warn("Job: " + name + " have no dueTime, create fail!");
						continue;
					} else {
						job.setExecuteTime(startTime);
					}
				} else {
					if (dueTime instanceof Date) {
						temp.setTimeInMillis(((Date) dueTime).getTime());
					} else if (dueTime instanceof Calendar) {
						temp.setTimeInMillis(((Calendar) dueTime).getTimeInMillis());
					} else if (dueTime instanceof Long) {
						temp.setTimeInMillis((Long) dueTime);
					} else if (dueTime instanceof String) {
						temp.setTimeInMillis(businessCalendar.add(temp.getTime(), (String) dueTime).getTime());
					} else {
						throw new RuntimeException("Unsupported DueTime format");
					}
					job.setExecuteTime(temp.getTime());
				}
				if (StringUtils.isNotEmpty(listener.getRepeatDuration())) {
					job.setRepeatDuration((String) getVariableValueByEL(listener.getRepeatDuration(), event, true));
				}
				if (StringUtils.isNotEmpty(listener.getRepeatTime())) {
					job.setRepeatTime(Integer.valueOf(getVariableValueByEL(listener.getRepeatTime(), event, true).toString()));
				}
				job.setState(state);
				jobContext.addJob(job);
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("there is no timeout listener in state[" + state.getName() + "].");
			}
		}
	}

	/**
	 * 把当前Event中的变量传播到别的Scope Context中
	 * <p>
	 * 该传播范围是在spring xml文件中定义的.
	 * 
	 * @see Propagation
	 * @param event
	 */
	protected void propagateVariables(Event event) {
		State state = event.getState();
		if (this.propagation == Propagation.STATE || this.propagation == Propagation.SESSION) {
			for (Variable var : event.getVariableSet()) {
				stateDao.setVariable(state, var.getName(), var.getValue());
			}
		}
		stateDao.save(state);
		Session session = state.getSession();
		if (this.propagation == Propagation.SESSION) {
			for (Variable var : event.getVariableSet()) {
				sessionDao.setVariable(session, var.getName(), var.getValue());
			}
		}
		session.setLastUpdateTime(new Date());
		sessionDao.save(session);
	}

	@Transactional
	public void handle(Event event) {
		try {
			// for the DefaultStateImpl we will go to the end
			if (log.isDebugEnabled()) {
				log.debug("receive event[" + event + "]");
			}
			propagateVariables(event);
			onEnd(event);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

	

	public List<StartListener> getStartListeners() {
		return startListeners;
	}

	public void setStartListeners(List<StartListener> startListeners) {
		this.startListeners = startListeners;
	}

	public List<EndListener> getEndListeners() {
		return endListeners;
	}

	public void setEndListeners(List<EndListener> endListeners) {
		this.endListeners = endListeners;
	}

	public Map<String, TimeOutListener> getTimeoutListeners() {
		return timeoutListeners;
	}

	public void setTimeoutListeners(Map<String, TimeOutListener> timeoutListeners) {
		this.timeoutListeners = timeoutListeners;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStateDao(StateDao stateDao) {
		this.stateDao = stateDao;
	}

	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		this.businessCalendar = businessCalendar;
	}

	public void setJobContext(JobContext jobContext) {
		this.jobContext = jobContext;
	}

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	protected Serializable getVariableValue(String variableName, Task task, boolean nested) {
		Serializable variableValue = this.getVariableValue(variableName, task.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}
		if (nested) {
			variableValue = this.getVariableValue(variableName, task.getState(), nested);
		}
		return variableValue;
	}

	protected Serializable getVariableValue(String variableName, Event event, boolean nested) {
		Serializable variableValue = this.getVariableValue(variableName, event.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}
		if (nested) {
			variableValue = this.getVariableValue(variableName, event.getState(), nested);
		}
		return variableValue;
	}

	protected Serializable getVariableValue(String variableName, State state, boolean nested) {
		Serializable variableValue = this.getVariableValue(variableName, state.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}
		if (nested) {
			variableValue = this.getVariableValue(variableName, state.getSession());
		}
		return variableValue;
	}

	protected Serializable getVariableValue(String variableName, Session session) {
		Serializable variableValue = this.getVariableValue(variableName, session.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}

		return variableValue;
	}

	protected Serializable getVariableValue(String variableName, Set<Variable> variableSet) {
		if (variableSet == null || variableName == null) {
			return null;
		}
		for (Variable var : variableSet) {
			if (var.getName().equals(variableName)) {
				return var.getValue();
			}
		}
		return null;
	}

	protected Serializable getVariableValueByEL(String variableName, Task task, boolean nested) {
		Serializable variableValue = this.getVariableValueByEL(variableName, task.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}
		if (nested) {
			variableValue = this.getVariableValueByEL(variableName, task.getState(), nested);
		}
		return variableValue;
	}

	protected Serializable getVariableValueByEL(String variableName, Event event, boolean nested) {
		Serializable variableValue = this.getVariableValueByEL(variableName, event.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}
		if (nested) {
			variableValue = this.getVariableValueByEL(variableName, event.getState(), nested);
		}
		return variableValue;
	}

	protected Serializable getVariableValueByEL(String variableName, State state, boolean nested) {
		Serializable variableValue = this.getVariableValueByEL(variableName, state.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}
		if (nested) {
			variableValue = this.getVariableValueByEL(variableName, state.getSession());
		}
		return variableValue;
	}

	protected Serializable getVariableValueByEL(String variableName, Session session) {
		Serializable variableValue = this.getVariableValueByEL(variableName, session.getVariableSet());
		if (variableValue != null) {
			return variableValue;
		}

		return variableValue;
	}

	protected Serializable getVariableValueByEL(String variableName, Set<Variable> variableSet) {
		if (variableSet == null || variableName == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (Variable var : variableSet) {
			map.put(var.getName(), var.getValue());
		}
		try {
			return (Serializable) MVEL.eval(variableName, map);
		} catch (Exception e) {

		}
		return null;
	}

	protected State getLatestState(Session session) {
		State lastest = null;
		for (State state : session.getStateSet()) {
			if (state == null) {
				continue;
			}
			if (lastest == null || lastest.getId() < state.getId()) {
				lastest = state;
			}
		}
		return lastest;
	}

}

// $Id$