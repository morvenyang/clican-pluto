/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service;

import java.util.List;

import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;

public interface DataStructureService {

	public Type getType(String name);

	public Control getControl(String name);

	public void save(ModelDescription modelDescription);

	public void delete(ModelDescription modelDescription);

	public void update(String oldModelDescName,
			ModelDescription modelDescription);

	public List<Type> getTypeList(Control control);

	public List<Control> getControlList();

	public void init();
	
	public void setModelContainer(ModelContainer modelContainer);

}

// $Id$