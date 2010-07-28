/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.interfaces;

import com.clican.pluto.cluster.exception.SynchronizeException;

public interface ISynchronizeService {

	/**
	 * Synchronize the message between all of cluster nodes.
	 * 
	 * @param msg
	 *            The message describe the data shall be synchronized.
	 * @throws SynchronizeException
	 *             If there is error occurred in synchronization procedure, this
	 *             exception will be thrown.
	 */
	public void synchronize(Message msg) throws SynchronizeException;

}

// $Id$