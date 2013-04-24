/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.enumeration;

public enum EventType {

	/**
	 * 定时任务触发的事件
	 */
	JOB("job"),

	/**
	 * 和任务状态更新相关的事件
	 */
	TASK("task"),

	/**
	 * 普通的事件
	 */
	NORMAL("normal");

	private String type;

	private EventType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static EventType convert(String type) {
		for (EventType member : values()) {
			if (member.getType().equals(type)) {
				return member;
			}
		}
		return null;
	}
}

// $Id$