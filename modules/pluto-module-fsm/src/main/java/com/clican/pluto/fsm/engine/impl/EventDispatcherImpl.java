/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.fsm.dao.EventDao;
import com.clican.pluto.fsm.dao.SessionDao;
import com.clican.pluto.fsm.engine.EngineContext;
import com.clican.pluto.fsm.engine.EventDispatcher;
import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.enumeration.Parameters;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Variable;

/**
 * Event的dispatch类,外不系统会直接调用该类的dispatch方法来触发一个状态基的Event.
 * <p>
 * Event种类包括普通的Event,一个任务结束的Event和定时线程触发的Event.
 * 
 * @author wei.zhang
 * 
 */
public class EventDispatcherImpl implements EventDispatcher {

	private final static Log log = LogFactory.getLog(EventDispatcherImpl.class);

	private EventDao eventDao;

	private EngineContext engineContext;

	private SessionDao sessionDao;

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}

	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	@Transactional
	
	public void dispatch(Long sessionId, Long stateId, EventType eventType, Map<String, Serializable> parameters) {
		try {
			Session session = engineContext.querySession(sessionId);

			// 是否即时提醒的标志，需要放在session中
			Serializable noticeImmediately = parameters.remove(Parameters.NOTICE_IMMEDIATELY.getParameter());
			if (noticeImmediately == null) {
				noticeImmediately = 0;
			}
			sessionDao.setVariable(session, Parameters.NOTICE_IMMEDIATELY.getParameter(), noticeImmediately);

			if (log.isDebugEnabled()) {
				StringBuffer debug = new StringBuffer();
				debug.append("dispatch event sessionId=[" + sessionId + "],stateId=[" + stateId + "],eventType=" + eventType.getType() + "\n");
				for (String param : parameters.keySet()) {
					debug.append(param + "=" + parameters.get(param) + "\n");
				}
				log.debug(debug.toString());
			}
			if (session == null) {
				throw new RuntimeException("The session doesn't exist");
			}
			Event event = new Event();
			event.setCompleteTime(new Date());
			State activeState = engineContext.findStateById(stateId);
			if (activeState == null) {
				throw new RuntimeException("There is no active state for this session [" + sessionId + "]");
			}
			event.setState(activeState);
			event.setEventType(eventType.getType());
			Set<Variable> vars = new HashSet<Variable>();
			for (String name : parameters.keySet()) {
				Variable var = new Variable();
				var.setEvent(event);
				var.setName(name);
				var.setValue(parameters.get(name));
				var.setChangeDate(new Date());
				vars.add(var);
			}
			event.setVariableSet(vars);
			IState istate = engineContext.getState(session.getName(), session.getVersion(), activeState.getName());
			eventDao.save(event);
			istate.handle(event);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

}

// $Id$