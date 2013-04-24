/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.jms;

import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.clican.pluto.cluster.exception.SynchronizeException;
import com.clican.pluto.cluster.interfaces.ISynchronizeService;
import com.clican.pluto.cluster.interfaces.Message;

public class SynchronizeServiceJmsImpl implements ISynchronizeService {

	private final static Log log = LogFactory
			.getLog(SynchronizeServiceJmsImpl.class);

	private Map<String, Destination> destinationMap;

	private JmsTemplate jmsTemplate;

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setDestinationMap(Map<String, Destination> destinationMap) {
		this.destinationMap = destinationMap;
	}

	public void synchronize(final Message msg) throws SynchronizeException {
		if (log.isDebugEnabled()) {
			log.debug("Send message [" + msg.toString() + "] to topic ["
					+ msg.getName() + "]");
		}
		Destination destination = destinationMap.get(msg.getName());
		if (destination == null) {
			throw new SynchronizeException(msg);
		}
		try {
			jmsTemplate.send(destination, new MessageCreator() {
				public javax.jms.Message createMessage(Session session)
						throws JMSException {
					ObjectMessage om = session.createObjectMessage();
					om.setObject(msg);
					return om;
				}
			});
		} catch (JmsException e) {
			// If the <code>JmsException</code> is thrown, we have to invoke the
			// onException method to inform the SimpleConnectionFactory to
			// re-get
			// connection from newer ConnectionFactory which is injected as an
			// target.
			ConnectionFactory cf = jmsTemplate.getConnectionFactory();
			if (cf instanceof ExceptionListener) {
				((ExceptionListener) cf).onException(new JMSException(e
						.getMessage()));
			}
			try {
				jmsTemplate.send(destination, new MessageCreator() {
					public javax.jms.Message createMessage(Session session)
							throws JMSException {
						ObjectMessage om = session.createObjectMessage();
						om.setObject(msg);
						return om;
					}
				});
			} catch (JmsException ex) {
				throw new SynchronizeException(ex, msg);
			}
		}

	}

}

// $Id$