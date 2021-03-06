/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import java.util.List;
import java.util.Set;

import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.desc.TemplateSitePair;

public interface ClassLoaderUtil {

	public Set<String> getRuntimeJars();

	public IDirectory newDirectory(IDirectory parent, String name);

	public IDataModel newDataModel(IDirectory parent,
			ModelDescription modelDescription);

	public void configureTemplateDirectorySiteRelations(IDataModel dataModel,
			List<TemplateSitePair> selectedTemplateSitePairs);

	public ITemplate newTemplate();
	
	public ISite newSite();

	public List<Class<?>> getAllDataModelClass();

	public Class<?> getClass(IPojo pojo);

	public Class<?> getClass(String modelClass);

	public Class<?> getClass(ModelDescription modelDescription);

}

// $Id$