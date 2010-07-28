/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.lock;

public class DefaultLock implements Lockable {

	private boolean lock;

	public void lock() {
		lock = true;
		while (lock) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {

			}
		}
	}

	public void unLock() {
		lock = false;
	}

	public boolean isLocked() {
		return lock;
	}

}

//$Id: DefaultLock.java 91 2009-04-19 13:00:37Z clican $