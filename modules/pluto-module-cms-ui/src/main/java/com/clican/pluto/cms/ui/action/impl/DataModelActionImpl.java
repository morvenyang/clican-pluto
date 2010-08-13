/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.action.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.ajax4jsf.component.html.Include;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.core.comparator.PropertyComparator;
import com.clican.pluto.cms.core.service.DataModelService;
import com.clican.pluto.cms.core.service.IssueService;
import com.clican.pluto.cms.ui.action.DataModelAction;
import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.common.util.BeanUtils;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

@Scope(ScopeType.PAGE)
@Name("dataModelAction")
public class DataModelActionImpl extends BaseAction implements DataModelAction {

	@In("#{issueService}")
	private IssueService issueService;

	@In("#{dataModelService}")
	private DataModelService dataModelService;

	@In("#{classLoaderUtil}")
	private ClassLoaderUtil classLoaderUtil;

	@Out(required = false)
	private List<ModelDescription> dataModelDescList;

	private ModelDescription modelDescription;

	private IDataModel dataModel;

	private IDirectory parentDirectory;

	private List<ModelDescription> modelDescriptionList;

	private Map<String, Map<IDataModel, Boolean>> dataModelSelection = new HashMap<String, Map<IDataModel, Boolean>>();

	@Factory("dataModelDescList")
	public void loadDataModelDescList() {
		dataModelDescList = dataModelService.findAllDataModelDesc();
		dataModelSelection.put("all", new HashMap<IDataModel, Boolean>());
		for (ModelDescription md : dataModelDescList) {
			dataModelSelection.put(md.getName(), new HashMap<IDataModel, Boolean>());
		}
		Collections.sort(dataModelDescList, new PropertyComparator<ModelDescription>("name"));
	}

	public void newDataModel(IDirectory parentDirectory, ModelDescription modelDescription) {
		this.dataModel = classLoaderUtil.newDataModel(parentDirectory, modelDescription);
		this.modelDescription = modelDescription;
		this.parentDirectory = parentDirectory;
		if (!dataModelSelection.containsKey(modelDescription.getName())) {
			dataModelSelection.put(modelDescription.getName(), new HashMap<IDataModel, Boolean>());
		}
		Include include = (Include) FacesContext.getCurrentInstance().getViewRoot().findComponent("workspace");
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		include.setViewId("http://localhost:8080/pluto/velocity/resource/newdatamodel.xhtml.vm;jsessionid=" + session.getId());
		session.setAttribute("propertyDescriptionList", modelDescription.getPropertyDescriptionList());
	}

	public void issue() {
		List<IDataModel> dataModels = new ArrayList<IDataModel>();
		for (Map<IDataModel, Boolean> selection : dataModelSelection.values()) {
			for (IDataModel dataModel : selection.keySet()) {
				if (selection.get(dataModel)) {
					dataModels.add(dataModel);
				}
			}
		}

		issueService.issue(dataModels);
	}

	public void issue(IDataModel dataModel) {
		issueService.issue(dataModel);
	}

	public void save() {
		dataModelService.save(dataModel);
		backToNonePage();
		clear();
	}

	public void delete(ModelDescription modelDescription) {
		String modelName = modelDescription == null ? "all" : modelDescription.getName();
		List<IDataModel> dataModels = new ArrayList<IDataModel>();
		for (IDataModel dataModel : dataModelSelection.get(modelName).keySet()) {
			if (dataModelSelection.get(modelName).get(dataModel)) {
				dataModels.add(dataModel);
			}
		}
		dataModelService.delete(dataModels, modelDescription);
	}

	public void showDataModels(IDirectory directory) {
		this.parentDirectory = directory;
		modelDescriptionList = dataModelService.getModelDescriptions(directory);
		Include include = (Include) FacesContext.getCurrentInstance().getViewRoot().findComponent("workspace");
		include.getChildren().clear();
		include.setViewId("datamodel.xhtml");
	}

	public List<IDataModel> getDataModels(ModelDescription modelDescription) {
		String modelName = modelDescription == null ? "all" : modelDescription.getName();
		List<IDataModel> dataModels = dataModelService.getDataModels(parentDirectory, modelDescription, null);
		if (!dataModelSelection.containsKey(modelName)) {
			dataModelSelection.put(modelName, new HashMap<IDataModel, Boolean>());
		}
		dataModelSelection.get(modelName).clear();
		for (IDataModel dataModel : dataModels) {
			dataModelSelection.get(modelName).put(dataModel, false);
		}
		return dataModels;
	}

	public void cancel() {
		backToNonePage();
		clear();
	}

	private void clear() {
		this.dataModel = null;
		this.modelDescription = null;
		parentDirectory = null;
		modelDescriptionList = null;
	}

	@SuppressWarnings("unchecked")
	public Object getProperty(IDataModel dataModel, String propertyName) {
		Object result = null;
		try {
			result = BeanUtils.getProperty(dataModel, propertyName);
			if (result instanceof IDataModel) {
				result = ((IDataModel) result).getName();
			} else if (result instanceof Collection<?>) {
				StringBuffer buf = new StringBuffer();
				for (IDataModel dm : (Collection<IDataModel>) result) {
					buf.append(dm.getName() + ",");
				}
				if (buf.length() > 0) {
					buf.substring(0, buf.length() - 1);
				}
				result = buf.toString();
			}
		} catch (Exception e) {
			throw new PlutoException(e);
		}
		return result;
	}

	public ModelDescription getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(ModelDescription modelDescription) {
		this.modelDescription = modelDescription;
	}

	public IDataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(IDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public IDirectory getParentDirectory() {
		return parentDirectory;
	}

	public void setParentDirectory(IDirectory parentDirectory) {
		this.parentDirectory = parentDirectory;
	}

	public List<ModelDescription> getModelDescriptionList() {
		return modelDescriptionList;
	}

	public void setModelDescriptionList(List<ModelDescription> modelDescriptionList) {
		this.modelDescriptionList = modelDescriptionList;
	}

	public Map<String, Map<IDataModel, Boolean>> getDataModelSelection() {
		return dataModelSelection;
	}

	public void setDataModelSelection(Map<String, Map<IDataModel, Boolean>> dataModelSelection) {
		this.dataModelSelection = dataModelSelection;
	}

}

// $Id$