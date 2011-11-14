/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action.model;

import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import org.ajax4jsf.component.html.Include;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.core.comparator.PropertyComparator;
import com.clican.pluto.cms.core.service.DataModelService;
import com.clican.pluto.cms.core.service.DataStructureService;
import com.clican.pluto.cms.ui.action.BaseAction;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.MutilValueControl;
import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.desc.PropertyDescription;

@Scope(ScopeType.PAGE)
@Name("dataStructureAction")
public class DataStructureAction extends BaseAction  {

	@In("#{dataStructureService}")
	private DataStructureService dataStructureService;

	@In("#{dataModelService}")
	private DataModelService dataModelService;

	private ModelDescription modelDescription;

	private String oldModelDescName;

	private Control control;

	@In(required = false)
	private List<ModelDescription> dataModelDescList;

	public void newDataStructure() {
		Include include = (Include) FacesContext.getCurrentInstance().getViewRoot().findComponent("workspace");
		include.setViewId("newdatastructure.xhtml");
		this.modelDescription = new ModelDescription();
	}

	public void addNewProperty() {
		modelDescription.getPropertyDescriptionList().add(new PropertyDescription());
	}

	public void save() {
		if (oldModelDescName != null) {
			dataStructureService.update(oldModelDescName, modelDescription);
		} else {
			dataStructureService.save(modelDescription);
		}

		dataModelDescList.clear();
		dataModelDescList.addAll(dataModelService.findAllDataModelDesc());
		Collections.sort(dataModelDescList, new PropertyComparator<ModelDescription>("name"));
		cancel();
	}

	public void edit(ModelDescription modelDescription) {
		try {
			this.modelDescription = modelDescription.getCloneBean();
			this.oldModelDescName = modelDescription.getName();
			Include include = (Include) FacesContext.getCurrentInstance().getViewRoot().findComponent("workspace");
			include.setViewId("newdatastructure.xhtml");
		} catch (Exception e) {
			throw new PlutoException(e);
		}
	}

	public void delete(ModelDescription modelDescription) {
		dataStructureService.delete(modelDescription);
		dataModelDescList.remove(modelDescription);
	}

	public void cancel() {
		backToNonePage();
		clear();
	}

	public void clear() {
		modelDescription = null;
	}

	public DataStructureService getDataStructureService() {
		return dataStructureService;
	}

	public DataModelService getDataModelService() {
		return dataModelService;
	}

	public ModelDescription getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(ModelDescription modelDescription) {
		this.modelDescription = modelDescription;
	}

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	public void editControlProperties(Control control) {
		this.control = control;
	}

	public void saveControlProperties() {
		this.control = null;
	}

	public boolean isMutilValueControl(Control control) {
		return (control instanceof MutilValueControl);
	}

}

// $Id$