/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.IssueService;
import com.clican.pluto.cms.dao.TemplateDao;
import com.clican.pluto.orm.annotation.DynamicModel;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ITemplate;
import com.clican.pluto.orm.dynamic.inter.ITemplateModelRelation;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;

public class IssueServiceImpl extends BaseService implements IssueService {

	private TemplateDao templateDao;

	private ModelContainer modelContainer;

	private ClassLoaderUtil classLoaderUtil;

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
		this.classLoaderUtil = classLoaderUtil;
	}

	@Transactional(readOnly = true)
	public void issue(IDataModel dataModel) {
		List<ITemplate> templates = templateDao.getSelectedTemplates(dataModel);
		for (ITemplate template : templates) {
			issue(template, dataModel);
		}
	}

	@Transactional(readOnly = true)
	public void issue(List<IDataModel> dataModels) {
		Map<ModelDescription, List<IDataModel>> dataModelMap = new HashMap<ModelDescription, List<IDataModel>>();
		for (IDataModel dataModel : dataModels) {
			ModelDescription md = modelContainer.getModelDesc(classLoaderUtil
					.getClass(dataModel).getAnnotation(DynamicModel.class)
					.name());
			if (!dataModelMap.containsKey(md)) {
				dataModelMap.put(md, new ArrayList<IDataModel>());
			}
			dataModelMap.get(md).add(dataModel);
		}
		for (ModelDescription md : dataModelMap.keySet()) {
			List<IDataModel> dataModelList = dataModelMap.get(md);
			for (int i = 0; i < dataModelList.size(); i = i + 1000) {
				int start = i;
				int end = i + 1000 > dataModelList.size() ? dataModelList
						.size() : i + 1000;
				List<ITemplateModelRelation> tmrList = templateDao
						.getTemplateModelRelations(dataModelList.subList(start,
								end), md);
				for (ITemplateModelRelation tmr : tmrList) {
					issue(tmr.getTemplate(), tmr.getDataModel());
				}
			}
		}
	}

	@Transactional(readOnly = true)
	public void issue(IDirectory directory, boolean recursion) {
		String pathExpression = directory.getPath();
		if (recursion) {
			pathExpression = pathExpression + "%";
		}
		for (ModelDescription md : modelContainer.getModelDescs()) {
			int count = templateDao.getTemplateModelRelationCount(md,
					pathExpression);
			for (int i = 0; i < count; i = i + 1000) {
				int start = i;
				int end = i + 1000 > count ? count : i + 1000;
				List<ITemplateModelRelation> tmrList = templateDao
						.getTemplateModelRelations(md, pathExpression, start,
								end);
				for (ITemplateModelRelation tmr : tmrList) {
					issue(tmr.getTemplate(), tmr.getDataModel());
				}
			}

		}
	}

	private void issue(ITemplate template, IDataModel dataModel) {
		Template t = null;
		Writer w = null;
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("this", dataModel);
		try {
			t = Velocity.getTemplate(template.getName() + ".vm");
			w = new OutputStreamWriter(
					new FileOutputStream("c:/aa/" + dataModel.getName() + "-"
							+ template.getName() + ".html"), "utf-8");
			t.merge(velocityContext, w);
			w.flush();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (w != null) {
					w.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
}

// $Id$