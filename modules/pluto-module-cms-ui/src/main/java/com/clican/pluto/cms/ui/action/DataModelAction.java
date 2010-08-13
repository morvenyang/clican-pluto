/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action;

import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

public interface DataModelAction {

	public void newDataModel(IDirectory parentDirectory, ModelDescription modelDescription);

	public void save();

	public void delete(ModelDescription modelDescription);

	public void showDataModels(IDirectory directory);

	public void issue();

	public void issue(IDataModel dataModel);

	public void editDataModel(IDataModel dataModel, ModelDescription modelDescription);

}

// $Id$