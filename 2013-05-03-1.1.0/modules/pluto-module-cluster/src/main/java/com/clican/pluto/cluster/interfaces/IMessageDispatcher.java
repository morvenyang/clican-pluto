/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.interfaces;

import com.clican.pluto.cluster.exception.SynchronizeException;

/**
 * The IMessageDispatcher interface defines message and handler mapping
 * management interface. The bind and unbind method can be used to manage
 * message and handler mapping.
 * 
 * @author clican
 * 
 */
public interface IMessageDispatcher {

	public void bind(String msgName, IMessageHandler handler);

	public void unbind(String msgName, IMessageHandler handler);

	/**
	 * Dispatch the message to corresponding <code>IMessageHandler</code>. If
	 * there are any error occurred in any one of the
	 * <code>IMessageHandler</code>. It must roll back all of pre executed
	 * handler.
	 * 
	 * <p>
	 * 
	 * And then throwing the <code>SynchronizeException</code>
	 * 
	 * @param msg
	 * @throws SynchronizeException
	 */
	public void dispatch(Message msg) throws SynchronizeException;

}

// $Id$