/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.enumeration;

/**
 * 默认情况下Directory上设置的Site和Template只会被一级子DataModel使用
 * 如果该Directory上设置的是EXTENDS状态的话，就可以继承上级Directory的设置，并且可以递归继承
 *
 * @author weizha
 *
 */
public enum IssueMode {

	OVERRIDE(0),
	
	EXTENDS(1);
	
	private int mode;
	
	private IssueMode(int mode){
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}
	
	
}


//$Id$