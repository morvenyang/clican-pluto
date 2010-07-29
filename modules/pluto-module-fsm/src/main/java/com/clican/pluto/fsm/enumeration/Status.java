/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.enumeration;

/**
 * 工作流<code>Session</code>,状态基<code>State</code>的状态枚举类
 * 
 *<p>
 * 
 * 对于<code>Session</code>来说只可能是active或inactive
 *<p>
 * 对于<code>State</code>来说三中状态都有可能
 * 
 * @author wei.zhang
 * 
 */
public enum Status {

	/**
	 * 激活状态,处于该状态的状态基可以处理Event
	 */
	ACTIVE("active"),

	/**
	 * 等待状态，处于该状态的状态基需要等到上面多个状态基结束后汇总到该状态基，该状态下也不能处理Event
	 */
	PENDING("pending"),

	/**
	 * 非激活状态，处于该状态的状态基不可以处理Event
	 */
	INACTIVE("inactive");

	private String status;

	private Status(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static Status convert(String status) {
		for (Status member : values()) {
			if (member.getStatus().equals(status)) {
				return member;
			}
		}
		return null;
	}

}

// $Id$