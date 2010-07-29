/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.IntroductionInterceptor;

import com.clican.pluto.fsm.engine.EngineContext;
import com.clican.pluto.fsm.enumeration.EventType;
import com.clican.pluto.fsm.interceptor.EventDispatchInterceptor;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.State;

/**
 * This class is used to implement the asynchronous event process.
 * <p>
 * It is implement by Spring AOP Framework. In the spring configuration file. We
 * shall configure which kind of interface invocation shall be interpreted by
 * this class. And then this class will use the <code>ThreadPoolExecutor</code>
 * to submit the invocation into a thread pool and return true/false
 * immediately.
 * </p>
 */
public class EventQueue implements IntroductionInterceptor {

	private final static Log log = LogFactory.getLog(EventQueue.class);

	/**
	 * 用来做锁，对于不同的sessionId来使用不同的锁，尽量避免互斥
	 */
	private final Object[] locks = new Object[] { new Object(), new Object(), new Object(), new Object(), new Object(), new Object(), new Object(),
			new Object(), new Object(), new Object() };

	private EngineContext engineContext;

	private List<EventDispatchInterceptor> interceptors;

	/**
	 * 用来拦截消息的传播
	 * 
	 * @param interceptors
	 */
	public void setInterceptors(List<EventDispatchInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	@SuppressWarnings("unchecked")
	public boolean implementsInterface(Class clazz) {
		return true;
	}

	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		if (log.isDebugEnabled()) {
			log.debug("Using the IntroductionInterceptor to interrupt the event [" + invocation.getMethod().getName() + "]");
		}

		Object result = null;
		Object[] args = invocation.getArguments();
		Long sessionId = (Long) args[0];
		Long stateId = (Long) args[1];
		Session session = engineContext.querySession(sessionId);
		State state = engineContext.findStateById(stateId);
		if (session == null) {
			throw new RuntimeException("The session doesn't exist sessionId[" + sessionId + "]");
		}
		if (state == null) {
			throw new RuntimeException("The state doesn't exist stateId[" + stateId + "]");
		} else {
			Object lock = locks[sessionId.intValue() % 10];
			if (log.isDebugEnabled()) {
				log.debug("Using lock for session[" + sessionId + "]");
			}
			synchronized (lock) {
				try {
					// 首先判断是否有消息拦截器
					if (interceptors != null) {
						for (EventDispatchInterceptor edi : interceptors) {
							edi.beforeDispatch(sessionId, stateId, (EventType) args[2], (Map<String, Serializable>) args[3]);
						}
					}
					boolean executed = false;
					List<State> activeStates = engineContext.getActiveAndPendingState(sessionId);
					for (State activeState : activeStates) {
						if (activeState.getId().equals(stateId)) {
							result = invocation.proceed();
							executed = true;
							break;
						}
					}
					if (!executed && log.isDebugEnabled()) {
						log.debug("The event shall be ignore for current active states[" + activeStates + "]");
					}
					return result;
				} catch (Throwable e) {
					throw e;
				} finally {
					if (log.isDebugEnabled()) {
						log.debug("Release lock for session[" + sessionId + "]");
					}
					if (interceptors != null) {
						// 倒序执行
						for (int i = interceptors.size(); i > 0; i--) {
							interceptors.get(i - 1).afterDispatch(sessionId, stateId, (EventType) args[2], (Map<String, Serializable>) args[3]);
						}
					}
				}
			}
		}

	}

}

// $Id$
