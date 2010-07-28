/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.TemplateService;
import com.clican.pluto.cms.dao.TemplateDao;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

public class TemplateServiceImpl extends BaseService implements TemplateService {

	private TemplateDao templateDao;

	private ClassLoaderUtil classLoaderUtil;

	private File templateStoragePath = new File("c:/templates");

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
		this.classLoaderUtil = classLoaderUtil;
	}

	public void setTemplateStoragePath(File templateStoragePath) {
		this.templateStoragePath = templateStoragePath;
	}

	@Transactional(readOnly = true)
	public List<ITemplate> getTemplates() {
		return templateDao.getTemplates();
	}

	@Transactional
	public void save(ITemplate template) {
		templateDao.save(template);
		createTemplateFile(template);
	}

	public ITemplate newTemplate() {
		return classLoaderUtil.newTemplate();
	}

	@Transactional(readOnly = true)
	public List<ITemplate> getSelectedTemplates(IDataModel dataModel) {
		return templateDao.getSelectedTemplates(dataModel);
	}

	@Transactional(readOnly = true)
	public void init() {
		for (ITemplate template : templateDao.getTemplates()) {
			createTemplateFile(template);
		}
	}

	private void createTemplateFile(ITemplate template) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(templateStoragePath.getAbsolutePath()
					+ "/" + template.getName() + ".vm");
			os.write(template.getContent().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
}

// $Id$