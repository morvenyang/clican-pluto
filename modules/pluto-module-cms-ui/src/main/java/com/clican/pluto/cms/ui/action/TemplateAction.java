/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action;

import com.clican.pluto.orm.dynamic.inter.ITemplate;

public interface TemplateAction {

	public void edit(ITemplate template);
	
	public void delete(ITemplate template);
}


//$Id$