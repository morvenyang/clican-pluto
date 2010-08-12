/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service;

import java.util.List;
import java.util.Map;

import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

public interface DataModelService {

	public List<ModelDescription> findAllDataModelDesc();

	public IDataModel newDataModel(IDirectory parent,
			ModelDescription modelDescription);

	public List<IDataModel> getDataModels(IDirectory parent,
			ModelDescription modelDescription, List<String> orderBy);

	public void save(IDataModel dataModel);
	
	public void save(Map<String,Object> dataModelMap,IDirectory parent,
			ModelDescription modelDescription);

	public void delete(List<IDataModel> dataModels,
			ModelDescription modelDescription);

	public void configureTemplates(IDataModel dataModel,
			List<ITemplate> selectedTemplates);

	public List<ModelDescription> getModelDescriptions(IDirectory directory);

	public List<IDataModel> getDataModels(String modelName, String name);

	public IDataModel loadDataModel(String modelClass, Long id);

}

// $Id$