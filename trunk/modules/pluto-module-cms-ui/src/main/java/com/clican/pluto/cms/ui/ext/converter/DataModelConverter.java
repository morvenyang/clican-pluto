/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
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

import com.clican.pluto.cms.core.service.DataModelService;
import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.orm.dynamic.inter.IDataModel;

@Name("dataModelConverter")
@Scope(ScopeType.STATELESS)
@BypassInterceptors
@Converter
public class DataModelConverter extends BaseConverter implements StateHolder {

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
			DataModelService dataModelService = (DataModelService) Constants.ctx
					.getBean("dataModelService");
			return dataModelService.loadDataModel(modelClass, new Long(value));
		}
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof IDataModel) {
			return ((IDataModel) value).getId().toString();
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