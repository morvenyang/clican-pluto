/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.cms.ui.ext.converter;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import com.clican.pluto.common.util.TypeUtils;

@Name("numberConverter")
@Scope(ScopeType.STATELESS)
@BypassInterceptors
@Converter
public class NumberConverter extends BaseConverter implements StateHolder {

	private String modelClass;

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		} else {
			try {
				return TypeUtils.stringToNumber(value,
						Class.forName(modelClass));
			} catch (Exception e) {
				log.error("", e);
			}
			return null;
		}
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof Number) {
			return value + "";
		} else {
			throw new ConverterException();
		}
	}

	private boolean transientFlag = false;

	public boolean isTransient() {
		return transientFlag;
	}

	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		modelClass = (String) values[0];
	}

	public Object saveState(FacesContext context) {
		Object values[] = new Object[1];
		values[0] = modelClass;
		return (values);
	}

	public void setTransient(boolean newTransientValue) {
		this.transientFlag = newTransientValue;
	}
}

// $Id$