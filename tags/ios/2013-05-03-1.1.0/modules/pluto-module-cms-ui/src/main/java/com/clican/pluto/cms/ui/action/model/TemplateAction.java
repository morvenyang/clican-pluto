/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import com.clican.pluto.cms.core.comparator.PropertyComparator;
import com.clican.pluto.cms.core.service.SiteService;
import com.clican.pluto.cms.core.service.TemplateService;
import com.clican.pluto.cms.ui.action.BaseAction;
import com.clican.pluto.cms.ui.ext.transfer.SelectItem2StringTransfer;
import com.clican.pluto.orm.desc.TemplateSiteIdPair;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

@Scope(ScopeType.PAGE)
@Name("templateAction")
public class TemplateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7209304814414586544L;

	@In("#{templateService}")
	private TemplateService templateService;

	@In("#{siteService}")
	private SiteService siteService;

	@Out(required = false)
	@In(required = false)
	private List<ITemplate> templateList;

	private ITemplate template;

	private IDataModel dataModel;

	private List<TemplateSiteIdPair> selectedIdPairs;

	private List<String> allTemplateList;

	private List<String> allSiteList;

	@Factory("templateList")
	public void loadTemplateList() {
		templateList = templateService.getAllTemplates();
	}

	public void newTemplate() {
		template = templateService.newTemplate();
	}

	public void save() {
		templateService.save(template);
		templateList.clear();
		templateList.addAll(templateService.getAllTemplates());
		Collections.sort(templateList,
				new PropertyComparator<ITemplate>("name"));
		cancel();
	}

	public void cancel() {
		clear();
	}

	public void edit(ITemplate template) {
		this.template = template;
	}

	public void delete(ITemplate template) {
		templateList.remove(template);
		templateService.delete(template);
	}

	private void clear() {
		template = null;
	}

	public void configureTemplateAndSite(IDataModel dataModel) {
		this.dataModel = dataModel;
		selectedIdPairs = templateService.getTemplateSiteIdPairs(dataModel);
		allTemplateList = new ArrayList<String>();
		allSiteList = new ArrayList<String>();
		CollectionUtils.collect(templateService.getAllTemplates(),
				new SelectItem2StringTransfer(), allTemplateList);
		CollectionUtils.collect(siteService.getAllSites(),
				new SelectItem2StringTransfer(), allSiteList);
	}

	public void addPair() {
		TemplateSiteIdPair pair = new TemplateSiteIdPair();
		selectedIdPairs.add(pair);
	}
	
	public void deletePair(TemplateSiteIdPair pair){
		selectedIdPairs.remove(pair);
	}

	public void saveTemplateAndSiteConfiguration() {
		templateService.configureTemplateDirectorySiteRelations(dataModel,
				selectedIdPairs);
		clear();
	}

	@BypassInterceptors
	public ITemplate getTemplate() {
		return template;
	}

	public void setTemplate(ITemplate template) {
		this.template = template;
	}

	@BypassInterceptors
	public List<TemplateSiteIdPair> getSelectedIdPairs() {
		return selectedIdPairs;
	}

	public void setSelectedIdPairs(List<TemplateSiteIdPair> selectedIdPairs) {
		this.selectedIdPairs = selectedIdPairs;
	}

	@BypassInterceptors
	public List<String> getAllTemplateList() {
		return allTemplateList;
	}

	public void setAllTemplateList(List<String> allTemplateList) {
		this.allTemplateList = allTemplateList;
	}

	@BypassInterceptors
	public List<String> getAllSiteList() {
		return allSiteList;
	}

	public void setAllSiteList(List<String> allSiteList) {
		this.allSiteList = allSiteList;
	}

}

// $Id$