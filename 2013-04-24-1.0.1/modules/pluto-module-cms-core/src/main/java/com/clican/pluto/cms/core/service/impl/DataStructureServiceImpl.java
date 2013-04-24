/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.DataStructureService;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.MutilValueControl;
import com.clican.pluto.common.type.CustomListType;
import com.clican.pluto.common.type.CustomType;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.desc.PropertyDescription;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.DynamicORMManage;
import com.clican.pluto.orm.dynamic.inter.IDataBaseOperation;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;
import com.clican.pluto.orm.dynamic.inter.SessionFactoryUpdate;

public class DataStructureServiceImpl extends BaseService implements DataStructureService {

	private List<Control> controlList;

	private Map<String, Type> typeMap = new HashMap<String, Type>();

	private Map<String, Control> controlMap = new HashMap<String, Control>();

	private ClassLoaderUtil classLoaderUtil;

	private DynamicORMManage dynamicORMManage;

	private SessionFactoryUpdate sessionFactoryUpdate;

	private ModelContainer modelContainer;

	private IDataBaseOperation dataBaseOperation;

	public void setSessionFactoryUpdate(SessionFactoryUpdate sessionFactoryUpdate) {
		this.sessionFactoryUpdate = sessionFactoryUpdate;
	}

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
		this.classLoaderUtil = classLoaderUtil;
	}

	public void setDynamicORMManage(DynamicORMManage dynamicORMManage) {
		this.dynamicORMManage = dynamicORMManage;
	}

	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	public void setDataBaseOperation(IDataBaseOperation dataBaseOperation) {
		this.dataBaseOperation = dataBaseOperation;
	}

	@Transactional
	public void save(ModelDescription modelDescription) {
		dynamicORMManage.generateORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
	}

	@Transactional
	public void update(String oldModelDescName, ModelDescription modelDescription) {
		ModelDescription oldOne = modelContainer.getModelDesc(oldModelDescName);
		ModelDescription newOne = modelDescription;
		dynamicORMManage.updateORM(oldOne, newOne);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.alterTable(cfg, oldOne, newOne);
	}

	@Transactional
	public void delete(ModelDescription modelDescription) {
		Map<ModelDescription, ModelDescription> updateModelMap = new HashMap<ModelDescription, ModelDescription>();
		for (ModelDescription md : modelContainer.getModelDescs()) {
			List<PropertyDescription> removeProp = new ArrayList<PropertyDescription>();
			for (PropertyDescription pd : md.getPropertyDescriptionList()) {
				Type type = pd.getType();
				if (type instanceof CustomType) {
					if (type.getDeclareString().equals(modelDescription.getFirstCharUpperName())) {
						removeProp.add(pd);
					}
				} else if (type instanceof CustomListType) {
					if (((CustomListType) type).getCustomType().getDeclareString().equals(modelDescription.getFirstCharUpperName())) {
						removeProp.add(pd);
					}
				}
			}
			if (removeProp.size() != 0) {
				ModelDescription oldOne = md;
				ModelDescription newOne = oldOne.getCloneBean();
				newOne.getPropertyDescriptionList().removeAll(removeProp);
				dynamicORMManage.updateORM(oldOne, newOne);
				updateModelMap.put(oldOne, newOne);
			}
		}
		dynamicORMManage.deleteORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		for (ModelDescription oldOne : updateModelMap.keySet()) {
			ModelDescription newOne = updateModelMap.get(oldOne);
			dataBaseOperation.alterTable(cfg, oldOne, newOne);
		}
		dataBaseOperation.dropTable(modelDescription);
	}

	public List<Type> getTypeList(Control control) {
		List<Type> result = new ArrayList<Type>();
		if (control == null) {
			return result;
		}
		if (control instanceof MutilValueControl && ((MutilValueControl) control).isDynamic()) {
			for (Class<?> clazz : classLoaderUtil.getAllDataModelClass()) {
				if (control.isSupportMutil()) {
					result.add(new CustomListType(new CustomType(clazz.getName())));
				} else {
					result.add(new CustomType(clazz.getName()));
				}
			}
		} else {
			result.addAll(control.getSupportTypeList());
		}
		return result;
	}

	public Control getControl(String name) {
		return controlMap.get(name);
	}

	public Type getType(String name) {
		if (typeMap.containsKey(name)) {
			return typeMap.get(name);
		}
		for (Class<?> clazz : classLoaderUtil.getAllDataModelClass()) {
			Type type = new CustomType(clazz.getName());
			if (type.getName().equals(name)) {
				return type;
			}
			type = new CustomListType(new CustomType(clazz.getName()));
			if (type.getName().equals(name)) {
				return type;
			}
		}
		return null;
	}

	public void setTypeList(List<Type> typeList) {
		for (Type type : typeList) {
			typeMap.put(type.getName(), type);
		}
	}

	public List<Control> getControlList() {
		return controlList;
	}

	public void setControlList(List<Control> controlList) {
		this.controlList = controlList;
		for (Control control : controlList) {
			controlMap.put(control.getName(), control);
		}
	}

}

// $Id$