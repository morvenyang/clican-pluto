/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.desc;

import com.clican.pluto.orm.dynamic.inter.ISite;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

public class TemplateSitePair {

	private ITemplate template;
	
	private ISite site;

	public ITemplate getTemplate() {
		return template;
	}

	public void setTemplate(ITemplate template) {
		this.template = template;
	}

	public ISite getSite() {
		return site;
	}

	public void setSite(ISite site) {
		this.site = site;
	}
	
	
}


//$Id$