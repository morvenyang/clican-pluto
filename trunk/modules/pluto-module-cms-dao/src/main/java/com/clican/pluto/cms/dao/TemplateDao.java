/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao;

import java.util.List;

import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.ITemplate;
import com.clican.pluto.orm.dynamic.inter.ITemplateModelRelation;

public interface TemplateDao extends Dao {

	public List<ITemplate> getTemplates();

	public List<ITemplate> getSelectedTemplates(IDataModel dataModel);

	public List<ITemplateModelRelation> getTemplateModelRelations(
			List<IDataModel> dataModels, ModelDescription modelDescription);

	public List<ITemplateModelRelation> getTemplateModelRelations(
			ModelDescription modelDescription, final String pathExpression,
			final int firstResult, final int maxResults);

	public int getTemplateModelRelationCount(ModelDescription modelDescription,
			String pathExpression);

}

// $Id$