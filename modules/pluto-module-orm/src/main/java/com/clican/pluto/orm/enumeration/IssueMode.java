/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.enumeration;

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