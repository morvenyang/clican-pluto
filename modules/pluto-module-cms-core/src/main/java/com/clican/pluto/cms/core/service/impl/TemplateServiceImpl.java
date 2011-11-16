/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.TemplateService;
import com.clican.pluto.cms.dao.DataModelDao;
import com.clican.pluto.cms.dao.SiteDao;
import com.clican.pluto.cms.dao.TemplateDao;
import com.clican.pluto.orm.desc.TemplateSitePair;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ISite;
import com.clican.pluto.orm.dynamic.inter.ITemplate;
import com.clican.pluto.orm.dynamic.inter.ITemplateDirectorySiteRelation;
import com.clican.pluto.orm.dynamic.inter.ITemplateModelSiteRelation;

public class TemplateServiceImpl extends BaseService implements TemplateService {

	private TemplateDao templateDao;

	private SiteDao siteDao;

	private DataModelDao dataModelDao;

	private ClassLoaderUtil classLoaderUtil;

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
		this.classLoaderUtil = classLoaderUtil;
	}

	public void setDataModelDao(DataModelDao dataModelDao) {
		this.dataModelDao = dataModelDao;
	}

	public void setSiteDao(SiteDao siteDao) {
		this.siteDao = siteDao;
	}

	@Transactional(readOnly = true)
	public List<ITemplate> getAllTemplates() {
		return templateDao.getAllTemplates();
	}

	@Transactional
	public void save(ITemplate template) {
		if (template.getId() != null) {
			templateDao.update(template);
		} else {
			templateDao.save(template);
		}
	}

	public ITemplate newTemplate() {
		return classLoaderUtil.newTemplate();
	}

	@Transactional(readOnly = true)
	public List<TemplateSitePair> getTemplateSitePairs(IDataModel dataModel) {
		List<TemplateSitePair> result = new ArrayList<TemplateSitePair>();
		if (dataModel instanceof IDirectory) {
			List<ITemplateDirectorySiteRelation> relationList = templateDao
					.getTemplateDirectorySiteRelations(dataModel);
			for (ITemplateDirectorySiteRelation rel : relationList) {
				TemplateSitePair pair = new TemplateSitePair();
				pair.setSite(rel.getSite());
				pair.setTemplate(rel.getTemplate());
				result.add(pair);
			}
		} else {
			templateDao.getTemplateModelSiteRelations(dataModel);
			List<ITemplateModelSiteRelation> relationList = templateDao
					.getTemplateModelSiteRelations(dataModel);
			for (ITemplateModelSiteRelation rel : relationList) {
				TemplateSitePair pair = new TemplateSitePair();
				pair.setSite(rel.getSite());
				pair.setTemplate(rel.getTemplate());
				result.add(pair);
			}
		}
		return result;
	}

	@Transactional
	public void delete(ITemplate template) {
		templateDao.delete(template);
	}

	@Transactional
	public void configureTemplateDirectorySiteRelations(IDataModel dataModel,
			List<TemplateSitePair> selectedTemplateSitePairs) {
		templateDao.deleteTemplateSiteRelation(dataModel);
		classLoaderUtil.configureTemplateDirectorySiteRelations(dataModel,
				selectedTemplateSitePairs);
		dataModelDao.update(dataModel);
	}

}

// $Id$