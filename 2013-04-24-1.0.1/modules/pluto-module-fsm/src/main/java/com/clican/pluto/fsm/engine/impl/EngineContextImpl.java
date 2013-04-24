/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.fsm.bean.Deploy;
import com.clican.pluto.fsm.dao.JobDao;
import com.clican.pluto.fsm.dao.SessionDao;
import com.clican.pluto.fsm.dao.StateDao;
import com.clican.pluto.fsm.dao.TaskDao;
import com.clican.pluto.fsm.engine.EngineContext;
import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.engine.state.StartStateImpl;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;
import com.clican.pluto.fsm.model.Variable;

/**
 * 该项目的主引擎驱动程序。 负责维护Event的先后循序
 * <p>
 * 工作流的描述的映射
 * <p>
 * 工作流Context的保存
 * <p>
 * 和一些常用的交互方法比如
 * <li>
 * newSession
 * <p>
 * getActiveState
 * <p>
 * querySession
 * <p>
 * queryTask</li>
 * 
 * @author wei.zhang
 * 
 */
public class EngineContextImpl implements EngineContext, ApplicationContextAware {

	private final static Log log = LogFactory.getLog(EngineContextImpl.class);

	private List<Deploy> deployList;

	private ApplicationContext applicationContext;

	/**
	 * Session Name和Session Version与对应的ApplicationContext的Map
	 */
	private Map<String, Map<Integer, ApplicationContext>> sessionSpringMap = new HashMap<String, Map<Integer, ApplicationContext>>();

	private Map<String, Integer> lastVersionMap = new HashMap<String, Integer>();

	private SessionDao sessionDao;

	private TaskDao taskDao;

	private StateDao stateDao;

	private JobDao jobDao;

	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setStateDao(StateDao stateDao) {
		this.stateDao = stateDao;
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public void setDeployList(List<Deploy> deployList) {
		this.deployList = deployList;
	}

	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	
	public IState getState(String sessionName, int sessionVersion, String stateName) {
		if (log.isDebugEnabled()) {
			log.debug("get state, sessionName=[" + sessionName + "],sessionVersion=[" + sessionVersion + "],stateName=[" + stateName + "]");
		}
		ApplicationContext context = sessionSpringMap.get(sessionName).get(sessionVersion);
		return (IState) context.getBean(stateName);
	}

	@Transactional(readOnly = true)
	
	public State findStateById(Long stateId) {
		return stateDao.findStateById(stateId);
	}

	@Transactional
	
	public Session newSession(String sessionName, String userId) {
		Session session = new Session();
		Integer lastVersion = this.lastVersionMap.get(sessionName);
		if (log.isDebugEnabled()) {
			log.debug("Create new session, sessionName=[" + sessionName + "],sessionVersion=[" + lastVersion + "],userId=[" + userId + "]");
		}
		ApplicationContext context = sessionSpringMap.get(sessionName).get(lastVersion);

		String[] names = context.getBeanNamesForType(StartStateImpl.class);
		if (names.length != 1) {
			throw new RuntimeException("There must be only one StartState in the session description file");
		}
		IState istate = (IState) context.getBean(names[0]);
		session.setStateSet(new HashSet<State>());
		session.setSponsor(userId);
		session.setStartTime(new Date());
		session.setLastUpdateTime(new Date());
		session.setStatus(Status.ACTIVE.getStatus());
		session.setName(sessionName);
		session.setVersion(lastVersionMap.get(sessionName));
		session.setVariableSet(new HashSet<Variable>());
		sessionDao.save(session);
		sessionDao.setVariable(session, Parameters.SPONSOR.getParameter(), userId);
		istate.onStart(session, null, null);
		return session;
	}

	@Transactional
	
	public Session newSubSession(Long parentSessionId, String subStateName) {
		Session parent = sessionDao.findSessionById(parentSessionId);
		if (parent == null) {
			throw new RuntimeException("The parent session doesn't exist [" + parentSessionId + "]");
		}
		Session session = new Session();
		IState istate = (IState) sessionSpringMap.get(parent.getName()).get(parent.getVersion()).getBean(subStateName);
		session.setStateSet(new HashSet<State>());
		session.setSponsor(parent.getSponsor());
		session.setStartTime(new Date());
		session.setLastUpdateTime(new Date());
		session.setStatus(Status.ACTIVE.getStatus());
		session.setName(parent.getName());
		session.setVersion(parent.getVersion());
		session.setVariableSet(new HashSet<Variable>());
		sessionDao.save(session);
		sessionDao.setVariable(session, Parameters.SPONSOR.getParameter(), parent.getSponsor());
		istate.onStart(null, null, null);
		return session;
	}

	@Transactional(readOnly = true)
	
	public Session querySession(Long sessionId) {
		return sessionDao.findSessionById(sessionId);
	}

	@Transactional(readOnly = true)
	
	public Task queryTask(Long taskId) {
		return taskDao.findTaskById(taskId);
	}

	public void start() {
		if (log.isInfoEnabled()) {
			log.info("Begin to start Finiate State Machine Engine Context");
		}
		for (Deploy deploy : deployList) {
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { deploy.getUrl() }, this.applicationContext);
			if (!sessionSpringMap.containsKey(deploy.getName())) {
				sessionSpringMap.put(deploy.getName(), new HashMap<Integer, ApplicationContext>());
			}
			sessionSpringMap.get(deploy.getName()).put(deploy.getVersion(), context);
			// 为了解决Lazy Init导致的一些问题，我们强迫所有的Bean被初始化出来
			for (String name : context.getBeanDefinitionNames()) {
				context.getBean(name);
			}
			if ((lastVersionMap.containsKey(deploy.getName()) && lastVersionMap.get(deploy.getName()).compareTo(deploy.getVersion()) < 0)
					|| !lastVersionMap.containsKey(deploy.getName())) {
				lastVersionMap.put(deploy.getName(), deploy.getVersion());
			}
		}
		if (log.isInfoEnabled()) {
			log.info("The Finiate State Machine Engine Context has been started successfully.");
		}
	}

	@Transactional(readOnly = true)
	
	public List<Task> queryTask(String userId, String sessionName, String stateName, boolean completed) {
		return taskDao.findTasksByParams(userId, sessionName, stateName, completed);
	}

	@Transactional(readOnly = true)
	
	public List<State> getActiveState(Long sessionId) {
		return stateDao.getActiveStates(sessionId);
	}

	@Transactional(readOnly = true)
	
	public List<State> getActiveAndPendingState(Long sessionId) {
		return stateDao.getActiveAndPendingState(sessionId);
	}

	@Transactional
	
	public void deleteSession(Long sessionId) {
		Session session = sessionDao.findSessionById(sessionId);
		if (session != null) {
			sessionDao.delete(session);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("The session[" + sessionId + "] is not found, we can't delete it");
			}
		}
	}

	@Transactional(readOnly = true)
	
	public Serializable getVariableValue(Long sessionId, String variableName) {
		return sessionDao.getVariableValue(sessionId, variableName);
	}

	@Transactional
	
	public void completeSession(Long sessionId) {
		if (log.isDebugEnabled()) {
			log.debug("complete session[" + sessionId + "]");
		}
		Session session = sessionDao.findSessionById(sessionId);
		if (session == null) {
			if (log.isDebugEnabled()) {
				log.debug("The session[" + sessionId + "] is not found, we can't complete it");
			}
			return;
		}
		session.setStatus(Status.INACTIVE.getStatus());
		session.setEndTime(new Date());
		sessionDao.save(session);

		jobDao.deleteJobsBySessionId(session.getId());
		if (log.isDebugEnabled()) {
			log.debug("delete session[" + sessionId + "] related jobs successfully");
		}
		stateDao.inactiveStatesBySessionId(session.getId());
		if (log.isDebugEnabled()) {
			log.debug("inactive session[" + sessionId + "] related states successfully");
		}
		taskDao.completeTasksBySessionId(session.getId());
		if (log.isDebugEnabled()) {
			log.debug("complete session[" + sessionId + "] related tasks successfully");
		}
	}

}

// $Id$