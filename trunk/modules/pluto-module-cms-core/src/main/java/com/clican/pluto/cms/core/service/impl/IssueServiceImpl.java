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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.DataModelService;
import com.clican.pluto.cms.core.service.IssueService;
import com.clican.pluto.cms.core.service.SiteService;
import com.clican.pluto.cms.dao.DataModelDao;
import com.clican.pluto.cms.dao.SiteDao;
import com.clican.pluto.cms.dao.TemplateDao;
import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.util.BeanUtils;
import com.clican.pluto.orm.annotation.DynamicModel;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.ISite;
import com.clican.pluto.orm.dynamic.inter.ITemplate;
import com.clican.pluto.orm.dynamic.inter.ITemplateDirectorySiteRelation;
import com.clican.pluto.orm.dynamic.inter.ITemplateModelSiteRelation;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;
import com.clican.pluto.orm.enumeration.IssueMode;
import com.clican.pluto.orm.enumeration.IssueStatus;
import com.clican.pluto.orm.model.IssueQueue;

public class IssueServiceImpl extends BaseService implements IssueService {

	private TemplateDao templateDao;

	private ModelContainer modelContainer;

	private ClassLoaderUtil classLoaderUtil;

	private DataModelService dataModelService;

	private SiteDao siteDao;

	private DataModelDao dataModelDao;

	private SiteService siteService;

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public void setSiteDao(SiteDao siteDao) {
		this.siteDao = siteDao;
	}

	public void setDataModelDao(DataModelDao dataModelDao) {
		this.dataModelDao = dataModelDao;
	}

	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
		this.classLoaderUtil = classLoaderUtil;
	}

	public void setDataModelService(DataModelService dataModelService) {
		this.dataModelService = dataModelService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	@Transactional(readOnly = true)
	public void issue(IDataModel dataModel) {
		List<IssueQueue> queue = new ArrayList<IssueQueue>();
		List<ITemplateModelSiteRelation> tmrList = templateDao
				.getTemplateModelSiteRelations(dataModel);
		for (ITemplateModelSiteRelation tmr : tmrList) {
			IssueQueue iq = convertToIssueQueue(tmr);
			queue.add(iq);
		}
		IDirectory directory = dataModel.getParent();
		while (directory != null) {
			List<ITemplateDirectorySiteRelation> tdrList = templateDao
					.getTemplateDirectorySiteRelations(directory);
			for (ITemplateDirectorySiteRelation tdr : tdrList) {
				IssueQueue iq = convertToIssueQueue(tdr, dataModel);
				queue.add(iq);
			}
			if (directory.getIssueMode() == IssueMode.EXTENDS.getMode()) {
				directory = directory.getParent();
			} else {
				break;
			}

		}
		this.addIntoIssueQueue(queue, true);
	}

	private IssueQueue convertToIssueQueue(ITemplateModelSiteRelation rel) {
		IssueQueue iq = new IssueQueue();
		iq.setDataModelId(rel.getDataModel().getId());
		iq.setDataModelName(rel.getDataModel().getClass()
				.getAnnotation(DynamicModel.class).name());
		iq.setIssueStatus(IssueStatus.NOT_ISSUE);
		iq.setRelativePath(rel.getRelativePath());
		iq.setSiteId(rel.getSite().getId());
		iq.setTemplateId(rel.getTemplate().getId());
		return iq;
	}

	private IssueQueue convertToIssueQueue(ITemplateDirectorySiteRelation rel,
			IDataModel dataModel) {
		IssueQueue iq = new IssueQueue();
		iq.setDataModelId(dataModel.getId());
		iq.setDataModelName(dataModel.getClass()
				.getAnnotation(DynamicModel.class).name());
		iq.setIssueStatus(IssueStatus.NOT_ISSUE);
		String path1 = rel.getDirectory().getPath();
		String path2 = dataModel.getParent().getPath();
		String diff = path2.replaceAll(path1, "");
		iq.setRelativePath(rel.getRelativePath() + diff);
		iq.setSiteId(rel.getSite().getId());
		iq.setTemplateId(rel.getTemplate().getId());
		return iq;
	}

	@Transactional(readOnly = true)
	public void issue(List<IDataModel> dataModels, IDirectory parentDirectory) {
		List<IssueQueue> queue = new ArrayList<IssueQueue>();
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
				List<ITemplateModelSiteRelation> tmrList = templateDao
						.getTemplateModelSiteRelations(
								dataModelList.subList(start, end), md);
				for (ITemplateModelSiteRelation tmr : tmrList) {
					IssueQueue iq = convertToIssueQueue(tmr);
					queue.add(iq);
				}
			}
		}
		while (parentDirectory != null) {
			List<ITemplateDirectorySiteRelation> tdrList = templateDao
					.getTemplateDirectorySiteRelations(parentDirectory);
			for (ITemplateDirectorySiteRelation tdr : tdrList) {
				for (IDataModel dataModel : dataModels) {
					IssueQueue iq = convertToIssueQueue(tdr, dataModel);
					queue.add(iq);
				}
			}
			if (parentDirectory.getIssueMode() == IssueMode.EXTENDS.getMode()) {
				parentDirectory = parentDirectory.getParent();
			} else {
				break;
			}

		}
		this.addIntoIssueQueue(queue, true);
	}

	@Transactional(readOnly = true)
	public void issue(IDirectory directory, boolean recursion) {
		List<IDataModel> dataModels = dataModelService.getDataModels(directory,
				null, null);
		issue(dataModels, directory);
		if (recursion) {
			for (IDirectory child : directory.getChildren()) {
				issue(child, recursion);
			}
		}
	}

	private void addIntoIssueQueue(List<IssueQueue> queue, boolean reverse) {
		Map<Long, ISite> siteMap = BeanUtils.convertToMap(
				siteDao.getAllSites(), "id");
		Map<Long, ITemplate> templateMap = BeanUtils.convertToMap(
				templateDao.getAllTemplates(), "id");
		if (reverse) {
			for (int i = queue.size() - 1; i >= 0; i--) {
				IssueQueue iq = queue.get(i);
				ISite site = siteMap.get(iq.getSiteId());
				ITemplate template = templateMap.get(iq.getTemplateId());
				IDataModel dataModel = dataModelDao.loadDataModel(
						classLoaderUtil
								.getClass(Constants.DYNAMIC_MODEL_PACKAGE + "."
										+ iq.getDataModelName()), iq
								.getDataModelId());
				issue(site, template, dataModel, iq.getRelativePath(), null);
			}
		} else {
			for (int i = 0; i < queue.size(); i++) {
				IssueQueue iq = queue.get(i);
				ISite site = siteMap.get(iq.getSiteId());
				ITemplate template = templateMap.get(iq.getTemplateId());
				IDataModel dataModel = dataModelDao.loadDataModel(
						classLoaderUtil
								.getClass(Constants.DYNAMIC_MODEL_PACKAGE + "."
										+ iq.getDataModelName()), iq
								.getDataModelId());
				issue(site, template, dataModel, iq.getRelativePath(), null);
			}
		}
	}

	private boolean issue(ISite site, ITemplate template, IDataModel dataModel,
			String relativePath, Map<ISite, FTPClient> ftpMap) {
		Template t = null;
		Writer w = null;
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("this", dataModel);
		FTPClient client = null;
		try {
			SimpleNode node = RuntimeSingleton.getRuntimeServices().parse(
					template.getContent(), template.getName());
			t = new Template();
			t.setName(template.getName());
			t.setRuntimeServices(RuntimeSingleton.getRuntimeServices());
			t.setData(node);
			t.initDocument();
			OutputStream os = null;
			if (site.getUrl().startsWith("ftp://")) {
				client = null;
				if (ftpMap != null) {
					client = ftpMap.get(site);
				}
				if (client == null) {
					client = siteService.getFTPClient(site);
				}
				if (client == null) {
					log.error("This site[" + site.getName()
							+ "] is unavailable");
					return false;
				}
				if (!relativePath.endsWith("/")) {
					relativePath = relativePath + "/";
				}
				os = client.appendFileStream(relativePath + dataModel.getName()
						+ "." + template.getSuffix());
			} else if (site.getUrl().startsWith("file://")) {
				String filePath = site.getUrl().substring(7);
				File file = new File(filePath + relativePath);
				if (file.exists()) {
					file.mkdirs();
				}
				os = new FileOutputStream(new File(file, dataModel.getName()
						+ "." + template.getSuffix()));
			} else {
				throw new UnknownHostException(site.getUrl());
			}
			w = new OutputStreamWriter(os, "utf-8");
			t.merge(velocityContext, w);
			w.flush();
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		} finally {
			try {
				if (w != null) {
					w.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}
			try {
				if (ftpMap == null && client != null) {
					client.logout();
					client.disconnect();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
}

// $Id$