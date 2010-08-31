/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.Include;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.core.comparator.PropertyComparator;
import com.clican.pluto.cms.core.service.TemplateService;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

@Scope(ScopeType.PAGE)
@Name("templateAction")
public class TemplateAction extends BaseAction {

	@In("#{templateService}")
	private TemplateService templateService;

	@Out(required = false)
	@In(required = false)
	private List<ITemplate> templateList;

	private ITemplate template;

	private IDataModel dataModel;

	private List<ITemplate> selectedTemplates;

	private List<ITemplate> remainedTemplates;

	@Factory("templateList")
	public void loadTemplateList() {
		templateList = templateService.getTemplates();
	}

	public void newTemplate() {
		Include include = (Include) FacesContext.getCurrentInstance().getViewRoot().findComponent("workspace");
		include.setViewId("newtemplate.xhtml");
		template = templateService.newTemplate();
	}

	public void save() {
		templateService.save(template);
		templateList.clear();
		templateList.addAll(templateService.getTemplates());
		Collections.sort(templateList, new PropertyComparator<ITemplate>("name"));
		cancel();
	}

	public void cancel() {
		backToNonePage();
		clear();
	}

	public void edit(ITemplate template) {
		this.template = template;
		Include include = (Include) FacesContext.getCurrentInstance().getViewRoot().findComponent("workspace");
		include.setViewId("newtemplate.xhtml");
	}

	public void delete(ITemplate template) {
		templateList.remove(template);
		templateService.delete(template);
	}

	private void clear() {
		template = null;
		remainedTemplates = null;
		selectedTemplates = null;
	}

	public void configureTemplate(IDataModel dataModel) {
		this.dataModel = dataModel;
		selectedTemplates = templateService.getSelectedTemplates(dataModel);
		remainedTemplates = new ArrayList<ITemplate>();
		for (ITemplate template : templateList) {
			if (!selectedTemplates.contains(template)) {
				remainedTemplates.add(template);
			}
		}
	}

	public void saveConfiguration() {
		templateService.configureTemplates(dataModel, selectedTemplates);
		clear();
	}

	public ITemplate getTemplate() {
		return template;
	}

	public void setTemplate(ITemplate template) {
		this.template = template;
	}

	public List<ITemplate> getSelectedTemplates() {
		return selectedTemplates;
	}

	public void setSelectedTemplates(List<ITemplate> selectedTemplates) {
		this.selectedTemplates = selectedTemplates;
	}

	public List<ITemplate> getRemainedTemplates() {
		return remainedTemplates;
	}

	public void setRemainedTemplates(List<ITemplate> remainedTemplates) {
		this.remainedTemplates = remainedTemplates;
	}
}

// $Id$