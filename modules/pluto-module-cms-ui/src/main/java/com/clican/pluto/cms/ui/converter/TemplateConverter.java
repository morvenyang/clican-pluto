/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.PageContext;

import com.clican.pluto.orm.dynamic.inter.ITemplate;

@Name("templateConverter")
@Scope(ScopeType.APPLICATION)
@BypassInterceptors
@Converter
public class TemplateConverter extends BaseConverter {

	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		PageContext pageContext = (PageContext) Contexts.getPageContext();
		List<ITemplate> list = (List<ITemplate>) pageContext
				.get("templateList");
		List<ITemplate> templateList = list;
		for (ITemplate template : templateList) {
			if (template.getName().equals(value)) {
				return template;
			}
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value instanceof ITemplate) {
			return ((ITemplate) value).getName();
		} else {
			return null;
		}
	}

}

// $Id$