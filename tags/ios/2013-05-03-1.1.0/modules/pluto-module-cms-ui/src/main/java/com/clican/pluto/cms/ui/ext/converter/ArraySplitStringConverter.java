/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.ext.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("assConverter")
@Scope(ScopeType.APPLICATION)
@BypassInterceptors
@Converter
public class ArraySplitStringConverter extends BaseConverter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			return value.split(",");
		}

	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {
			return null;
		}
		if (value.getClass().isArray()) {
			return com.clican.pluto.common.util.StringUtils
					.getSymbolSplitString((Object[]) value, ",");
		} else {
			throw new ConverterException();
		}
	}

}

// $Id$