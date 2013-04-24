/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.lock;

public interface Lockable {

	public void lock();

	public void unLock();
	
	public boolean isLocked();

}

//$Id: Lockable.java 91 2009-04-19 13:00:37Z clican $