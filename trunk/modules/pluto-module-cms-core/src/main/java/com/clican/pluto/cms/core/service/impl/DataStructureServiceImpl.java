/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.hibernate.cfg.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.DataStructureService;
import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.MutilValueControl;
import com.clican.pluto.common.exception.ORMManageException;
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
import com.clican.pluto.transaction.connections.XAFileSetConnection;

public class DataStructureServiceImpl extends BaseService implements
		DataStructureService {

	private List<Control> controlList;

	private Map<String, Type> typeMap = new HashMap<String, Type>();

	private Map<String, Control> controlMap = new HashMap<String, Control>();

	private ClassLoaderUtil classLoaderUtil;

	private DynamicORMManage dynamicORMManage;

	private SessionFactoryUpdate sessionFactoryUpdate;

	private Template dataStructureXHTMLTemplate;

	private ModelContainer modelContainer;

	private IDataBaseOperation dataBaseOperation;

	private XAFileSetConnection dataStructureXHTMLConnection;

	public void setSessionFactoryUpdate(
			SessionFactoryUpdate sessionFactoryUpdate) {
		this.sessionFactoryUpdate = sessionFactoryUpdate;
	}

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
		this.classLoaderUtil = classLoaderUtil;
	}

	public void setDynamicORMManage(DynamicORMManage dynamicORMManage) {
		this.dynamicORMManage = dynamicORMManage;
	}

	public void setDataStructureXHTMLTemplate(
			Template dataStructureXHTMLTemplate) {
		this.dataStructureXHTMLTemplate = dataStructureXHTMLTemplate;
	}

	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	public void setDataBaseOperation(IDataBaseOperation dataBaseOperation) {
		this.dataBaseOperation = dataBaseOperation;
	}

	public void setDataStructureXHTMLConnection(
			XAFileSetConnection dataStructureXHTMLConnection) {
		this.dataStructureXHTMLConnection = dataStructureXHTMLConnection;
	}

	@Transactional
	public void save(ModelDescription modelDescription) {
		dataStructureXHTMLConnection.connect();
		generateDataStrucutreXHTMLPage(modelDescription);
		dynamicORMManage.generateORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
	}

	@Transactional
	public void update(String oldModelDescName,
			ModelDescription modelDescription) {
		ModelDescription oldOne = modelContainer.getModelDesc(oldModelDescName);
		ModelDescription newOne = modelDescription;
		generateDataStrucutreXHTMLPage(newOne);
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
					if (type.getDeclareString().equals(
							modelDescription.getFirstCharUpperName())) {
						removeProp.add(pd);
					}
				} else if (type instanceof CustomListType) {
					if (((CustomListType) type).getCustomType()
							.getDeclareString().equals(
									modelDescription.getFirstCharUpperName())) {
						removeProp.add(pd);
					}
				}
			}
			if (removeProp.size() != 0) {
				ModelDescription oldOne = md;
				ModelDescription newOne = oldOne.getCloneBean();
				newOne.getPropertyDescriptionList().removeAll(removeProp);
				generateDataStrucutreXHTMLPage(newOne);
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
		if (control instanceof MutilValueControl
				&& ((MutilValueControl) control).isDynamic()) {
			for (Class<?> clazz : classLoaderUtil.getAllDataModelClass()) {
				if (control.isSupportMutil()) {
					result.add(new CustomListType(new CustomType(clazz
							.getName())));
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

	@Transactional
	public void init() {
		if (!Constants.DATA_STRUCTURE_XHTML_FOLDER.exists()) {
			Constants.DATA_STRUCTURE_XHTML_FOLDER.mkdirs();
		}
		dataStructureXHTMLConnection.connect();
		for (ModelDescription modelDescription : modelContainer.getModelDescs()) {
			generateDataStrucutreXHTMLPage(modelDescription);
		}
	}

	private void generateDataStrucutreXHTMLPage(
			ModelDescription modelDescription) {
		Writer w = null;
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("propertyDescriptionList", modelDescription
				.getPropertyDescriptionList());
		try {
			w = new OutputStreamWriter(dataStructureXHTMLConnection
					.getXAResource().getOutputStream(
							new File(Constants.DATA_STRUCTURE_XHTML_FOLDER
									+ "/" + modelDescription.getName()
									+ ".xhtml")), "utf-8");
			dataStructureXHTMLTemplate.merge(velocityContext, w);
			w.flush();
		} catch (Exception e) {
			throw new ORMManageException(e);
		} finally {
			try {
				if (w != null) {
					w.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}

// $Id$