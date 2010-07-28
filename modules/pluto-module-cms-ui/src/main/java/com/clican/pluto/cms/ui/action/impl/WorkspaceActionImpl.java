/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action.impl;

import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.Include;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.ui.action.WorkspaceAction;

@Scope(ScopeType.PAGE)
@Name("workspaceAction")
public class WorkspaceActionImpl extends BaseAction implements WorkspaceAction {

	public void changeCurrentViewId(String viewId) {
		Include include = (Include) FacesContext.getCurrentInstance()
				.getViewRoot().findComponent("workspace");
		include.setViewId(viewId);
	}

}

// $Id$