/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import java.util.Collection;

import com.clican.pluto.orm.desc.ModelDescription;

public interface ModelContainer {

	public void init();

	public void add(ModelDescription modelDescription);

	public void remove(ModelDescription modelDescription);

	public void update(ModelDescription oldOne, ModelDescription newOne);

	public ModelDescription getModelDesc(String modelOrSimpleClassName);

	public Collection<ModelDescription> getModelDescs();

}

// $Id$