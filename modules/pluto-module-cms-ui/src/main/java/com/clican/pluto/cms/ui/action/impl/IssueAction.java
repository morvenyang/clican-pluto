/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action.impl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.core.service.IssueService;
import com.clican.pluto.orm.dynamic.inter.IDataModel;

@Scope(ScopeType.EVENT)
@Name("issueAction")
public class IssueAction extends BaseAction  {

	@In("#{issueService}")
	private IssueService issueService;

	public void issue(IDataModel dataModel) {
		issueService.issue(dataModel);
	}
}

// $Id$