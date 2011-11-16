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
import com.clican.pluto.orm.dynamic.inter.ITemplateDirectorySiteRelation;
import com.clican.pluto.orm.dynamic.inter.ITemplateModelSiteRelation;

public interface TemplateDao extends Dao {

	public List<ITemplate> getAllTemplates();

	public List<ITemplate> getSelectedTemplates(IDataModel dataModel);

	public List<ITemplateModelSiteRelation> getTemplateModelSiteRelations(IDataModel dataModel);
	
	public List<ITemplateDirectorySiteRelation> getTemplateDirectorySiteRelations(IDataModel dataModel);
	
	public List<ITemplateModelSiteRelation> getTemplateModelSiteRelations(List<IDataModel> dataModels, ModelDescription modelDescription);

	public List<ITemplateModelSiteRelation> getTemplateModelSiteRelations(ModelDescription modelDescription, final String pathExpression, final int firstResult,
			final int maxResults);

	public int getTemplateModelSiteRelationCount(ModelDescription modelDescription, String pathExpression);

	public void deleteTemplateSiteRelation(IDataModel dataModel);
}

// $Id$