/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.cluster.exception.SynchronizeException;
import com.clican.pluto.cluster.interfaces.IMessageDispatcher;
import com.clican.pluto.cluster.interfaces.IMessageHandler;
import com.clican.pluto.cluster.interfaces.Message;

public abstract class BaseMessageDispatcher implements IMessageDispatcher {

	protected final Log log = LogFactory.getLog(getClass());

	protected Map<String, ConcurrentLinkedQueue<IMessageHandler>> messageHandlerMapping = new HashMap<String, ConcurrentLinkedQueue<IMessageHandler>>();

	public synchronized void bind(String msgName, IMessageHandler handler) {
		if (StringUtils.isEmpty(msgName) || handler == null) {
			throw new IllegalArgumentException(
					"The message name and message handler can't be null");
		}
		if (messageHandlerMapping.get(msgName) == null) {
			messageHandlerMapping.put(msgName,
					new ConcurrentLinkedQueue<IMessageHandler>());
		}
		messageHandlerMapping.get(msgName).add(handler);
	}

	public synchronized void unbind(String msgName, IMessageHandler handler) {
		if (StringUtils.isEmpty(msgName) || handler == null) {
			throw new IllegalArgumentException(
					"The message name and message handler can't be null");
		}
		ConcurrentLinkedQueue<IMessageHandler> handlerQueue = messageHandlerMapping
				.get(msgName);
		if (handlerQueue == null || !handlerQueue.contains(handler)) {
			log.warn("The message handler [" + handler + "] is not binded by ["
					+ msgName + "]");
		}
		handlerQueue.remove(handler);
	}

	public void dispatch(Message msg) throws SynchronizeException {
		String msgName = msg.getName();
		ConcurrentLinkedQueue<IMessageHandler> handlerQueue = messageHandlerMapping
				.get(msgName);
		if (handlerQueue != null && handlerQueue.size() != 0) {
			Iterator<IMessageHandler> it = handlerQueue.iterator();
			List<IMessageHandler> executedHandlerList = new ArrayList<IMessageHandler>();
			while (it.hasNext()) {
				try {
					IMessageHandler handler = it.next();
					handler.handle(msg);
					executedHandlerList.add(handler);
				} catch (SynchronizeException e) {
					for (IMessageHandler handler : executedHandlerList) {
						try {
							handler.rollback(msg);
						} catch (Throwable t) {
							log.error("", e);
						}
					}
					throw e;
				}
			}
		}
	}
}

// $Id$