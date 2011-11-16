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
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.TemplateService;
import com.clican.pluto.cms.dao.DataModelDao;
import com.clican.pluto.cms.dao.SiteDao;
import com.clican.pluto.cms.dao.TemplateDao;
import com.clican.pluto.common.util.BeanUtils;
import com.clican.pluto.orm.desc.TemplateSiteIdPair;
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
	public List<TemplateSiteIdPair> getTemplateSiteIdPairs(IDataModel dataModel) {
		List<TemplateSiteIdPair> result = new ArrayList<TemplateSiteIdPair>();
		if (dataModel instanceof IDirectory) {
			List<ITemplateDirectorySiteRelation> relationList = templateDao
					.getTemplateDirectorySiteRelations((IDirectory)dataModel);
			for (ITemplateDirectorySiteRelation rel : relationList) {
				TemplateSiteIdPair pair = new TemplateSiteIdPair();
				pair.setSiteId(rel.getSite().getId());
				pair.setTemplateId(rel.getTemplate().getId());
				result.add(pair);
			}
		} else {
			templateDao.getTemplateModelSiteRelations(dataModel);
			List<ITemplateModelSiteRelation> relationList = templateDao
					.getTemplateModelSiteRelations(dataModel);
			for (ITemplateModelSiteRelation rel : relationList) {
				TemplateSiteIdPair pair = new TemplateSiteIdPair();
				pair.setSiteId(rel.getSite().getId());
				pair.setTemplateId(rel.getTemplate().getId());
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
			List<TemplateSiteIdPair> selectedTemplateSiteIdPairs) {
		templateDao.deleteTemplateSiteRelation(dataModel);
		List<ISite> siteList = siteDao.getAllSites();
		Map<Long, ISite> siteMap = BeanUtils.convertToMap(siteList, "id");

		List<ITemplate> templateList = templateDao.getAllTemplates();
		Map<Long, ITemplate> templateMap = BeanUtils.convertToMap(templateList,
				"id");

		List<TemplateSitePair> selectedTemplateSitePairs = new ArrayList<TemplateSitePair>();
		for (TemplateSiteIdPair idPair : selectedTemplateSiteIdPairs) {
			TemplateSitePair pair = new TemplateSitePair();
			ITemplate template = templateMap.get(idPair.getTemplateId());
			ISite site = siteMap.get(idPair.getSiteId());
			if (template != null && site != null) {
				pair.setSite(site);
				pair.setTemplate(template);
				selectedTemplateSitePairs.add(pair);
			}
		}
		classLoaderUtil.configureTemplateDirectorySiteRelations(dataModel,
				selectedTemplateSitePairs);
		dataModelDao.update(dataModel);
	}

}

// $Id$