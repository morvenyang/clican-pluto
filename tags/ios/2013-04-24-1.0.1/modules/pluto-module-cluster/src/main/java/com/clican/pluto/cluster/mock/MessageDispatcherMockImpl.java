/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.mock;

import com.clican.pluto.cluster.exception.SynchronizeException;
import com.clican.pluto.cluster.interfaces.IMessageDispatcher;
import com.clican.pluto.cluster.interfaces.IMessageHandler;
import com.clican.pluto.cluster.interfaces.Message;

public class MessageDispatcherMockImpl implements IMessageDispatcher {

	public void bind(String msgName, IMessageHandler handler) {
		// TODO Auto-generated method stub

	}

	public void dispatch(Message msg) throws SynchronizeException {
		// TODO Auto-generated method stub

	}

	public void unbind(String msgName, IMessageHandler handler) {
		// TODO Auto-generated method stub

	}

}


//$Id$