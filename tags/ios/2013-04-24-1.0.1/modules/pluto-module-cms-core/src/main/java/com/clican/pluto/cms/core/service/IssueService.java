/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service;

import java.util.List;

import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

public interface IssueService {

	public void issue(IDataModel dataModel);

	public void issue(List<IDataModel> dataModels, IDirectory parentDirectory);

	public void issue(IDirectory directory, boolean recursion);

}

// $Id$