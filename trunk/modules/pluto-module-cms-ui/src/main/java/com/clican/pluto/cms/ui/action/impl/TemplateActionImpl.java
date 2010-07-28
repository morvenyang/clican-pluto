/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action.impl;

import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.Include;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.core.service.TemplateService;
import com.clican.pluto.cms.ui.action.TemplateAction;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

@Scope(ScopeType.PAGE)
@Name("templateAction")
public class TemplateActionImpl extends BaseAction implements TemplateAction {

	@In("#{templateService}")
	private TemplateService templateService;

	@Out(required = false)
	private List<ITemplate> templateList;

	private ITemplate template;

	@Factory("templateList")
	public void loadTemplateList() {
		templateList = templateService.getTemplates();
	}

	public void newTemplate() {
		Include include = (Include) FacesContext.getCurrentInstance()
				.getViewRoot().findComponent("workspace");
		include.setViewId("newtemplate.xhtml");
		template = templateService.newTemplate();
	}

	public void save() {
		templateService.save(template);
		templateList.add(template);
		backToNonePage();
		clear();
	}

	public void cancel() {
		backToNonePage();
		clear();
	}

	

	private void clear() {
		template = null;
	}


	public ITemplate getTemplate() {
		return template;
	}

	public void setTemplate(ITemplate template) {
		this.template = template;
	}

	

}

// $Id$