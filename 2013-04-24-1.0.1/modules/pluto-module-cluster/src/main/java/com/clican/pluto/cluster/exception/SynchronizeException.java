/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.exception;

import com.clican.pluto.cluster.interfaces.Message;
import com.clican.pluto.common.exception.PlutoException;

public class SynchronizeException extends PlutoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -611725289976794835L;

	public SynchronizeException(Throwable e, Message msg) {
		super("There is so error occurred when sending message [" + msg + "].",
				e);
	}

	public SynchronizeException(Message msg) {
		super("There is so error occurred when sending message [" + msg + "].");
	}

}

// $Id$