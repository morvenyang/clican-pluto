/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.interfaces;

import com.clican.pluto.cluster.exception.SynchronizeException;
import com.clican.pluto.common.exception.RollbackException;

/**
 * The subclass shall implement the handle interface to process the message from
 * other cluster nodes.
 * 
 * 
 * @author clican
 * 
 */
public interface IMessageHandler {

	/**
	 * Handle the message comes from other cluster nodes.
	 * 
	 * @param msg
	 * @throws SynchronizeException
	 *             If the message can't be processed correctly, this exception
	 *             will be thrown.
	 */
	public void handle(Message msg) throws SynchronizeException;

	/**
	 * Roll back current thread handled message process.
	 * 
	 * @param msg
	 * @throws RollbackException
	 */
	public void rollback(Message msg) throws RollbackException;

}

// $Id$