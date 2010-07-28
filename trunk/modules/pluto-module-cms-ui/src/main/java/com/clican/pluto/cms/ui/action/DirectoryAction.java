/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action;

import java.io.Serializable;

public interface DirectoryAction {
	
	public void delete(Serializable directory);
	
	public void save();
	
	public void update(Serializable directory);
	
}


//$Id$