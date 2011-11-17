/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.enumeration;

public enum IssueMode {

	NOT_EXTENDS(0),
	
	/**
	 * 在该Directory上设置的模板和站点是可以被子Directory和Model使用的
	 */
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