/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action;

import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.Include;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseAction {

	protected Log log = LogFactory.getLog(getClass());

	protected void backToNonePage() {
		Include include = (Include) FacesContext.getCurrentInstance()
				.getViewRoot().findComponent("workspace");
		include.setViewId("none.xhtml");
	}

}

// $Id$