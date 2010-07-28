/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action;

import com.clican.pluto.cms.core.service.DataStructureService;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.orm.desc.ModelDescription;

public interface DataStructureAction {

	public void newDataStructure();

	public void addNewProperty();

	public void save();

	public void edit(ModelDescription modelDescription);

	public void delete(ModelDescription modelDescription);

	public DataStructureService getDataStructureService();

	public void editControlProperties(Control control);

	public void saveControlProperties();

	public boolean isMutilValueControl(Control control);
}

// $Id$