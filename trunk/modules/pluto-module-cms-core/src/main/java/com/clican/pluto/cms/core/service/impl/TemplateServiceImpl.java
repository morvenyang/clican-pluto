/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.TemplateService;
import com.clican.pluto.cms.dao.DataModelDao;
import com.clican.pluto.cms.dao.TemplateDao;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

public class TemplateServiceImpl extends BaseService implements TemplateService {

	private TemplateDao templateDao;

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

	@Transactional(readOnly = true)
	public List<ITemplate> getTemplates() {
		return templateDao.getTemplates();
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
	public List<ITemplate> getSelectedTemplates(IDataModel dataModel) {
		return templateDao.getSelectedTemplates(dataModel);
	}

	@Transactional
	public void delete(ITemplate template) {
		templateDao.delete(template);
	}

	@Transactional
	public void configureTemplates(IDataModel dataModel, List<ITemplate> selectedTemplates) {
		templateDao.deleteTemplateRelation(dataModel);
		classLoaderUtil.configureTemplates(dataModel, selectedTemplates);
		dataModelDao.update(dataModel);
	}

}

// $Id$