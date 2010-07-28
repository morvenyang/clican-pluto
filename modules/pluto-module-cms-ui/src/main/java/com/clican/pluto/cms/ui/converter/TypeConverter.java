/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import com.clican.pluto.cms.core.service.DataStructureService;
import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.type.Type;

@Name("typeConverter")
@Scope(ScopeType.APPLICATION)
@BypassInterceptors
@Converter
public class TypeConverter extends BaseConverter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		DataStructureService dataStructureService = (DataStructureService) Constants.ctx
				.getBean("dataStructureService");
		Type type = dataStructureService.getType(value);
		if (type == null) {
			throw new ConverterException();
		} else {
			return type;
		}
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value instanceof Type) {
			Type type = (Type) value;
			return type.getName();
		} else if (value == null) {
			return null;
		} else {
			return "";
			//throw new ConverterException();
		}
	}

}

// $Id$