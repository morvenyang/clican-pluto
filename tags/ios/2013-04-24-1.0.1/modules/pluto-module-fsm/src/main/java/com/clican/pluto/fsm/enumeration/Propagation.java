/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.enumeration;

/**
 * 事件变量的传播类型的枚举类
 * 
 * @author wei.zhang
 * 
 */
public enum Propagation {

	/**
	 * 只在当前Event Scope Context中传播
	 */
	EVENT("event"),
	
	/**
	 * 只在当前Event Scope Context中传播
	 */
	TASK("task"),

	/**
	 * 传播到当前Event所对应的State Scope Context
	 */
	STATE("state"),

	/**
	 * 传播到当前Event所对应的State和Session Scope Context
	 */
	SESSION("session");

	private String propagation;

	private Propagation(String propagation) {
		this.propagation = propagation;
	}

	public static Propagation convert(String propagation) {
		for (Propagation member : values()) {
			if (member.propagation.equals(propagation)) {
				return member;
			}
		}
		return null;
	}

	public String getPropagation() {
		return propagation;
	}

}

// $Id$