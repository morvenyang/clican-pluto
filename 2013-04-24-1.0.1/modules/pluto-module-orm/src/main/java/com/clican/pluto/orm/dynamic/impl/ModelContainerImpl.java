/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.impl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.type.CustomType;
import com.clican.pluto.orm.annotation.DynamicModel;
import com.clican.pluto.orm.annotation.DynamicProperty;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.desc.PropertyDescription;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;

public class ModelContainerImpl implements ModelContainer {

	private Map<String, ModelDescription> modelMapping = new ConcurrentHashMap<String, ModelDescription>();

	private DynamicClassLoader dynamicClassLoader;

	public void setDynamicClassLoader(DynamicClassLoader dynamicClassLoader) {
		this.dynamicClassLoader = dynamicClassLoader;
	}

	public void add(ModelDescription modelDescription) {
		modelMapping.put(modelDescription.getName(), modelDescription);
	}

	public ModelDescription getModelDesc(String modelOrSimpleClassName) {
		ModelDescription md = modelMapping.get(modelOrSimpleClassName);
		if (md == null) {
			for (String name : modelMapping.keySet()) {
				if (name.equals(modelOrSimpleClassName.substring(0, 1)
						.toUpperCase()
						+ modelOrSimpleClassName.substring(1))) {
					md = modelMapping.get(name);
					break;
				}
			}
		}
		return md;
	}

	public void init() {
		Collection<Class<?>> loaderDynamicClasses = dynamicClassLoader
				.getLoadedDynamicClasses();
		for (Class<?> clazz : loaderDynamicClasses) {
			if (clazz.isAnnotationPresent(DynamicModel.class)) {
				ModelDescription modelDescription = new ModelDescription();
				DynamicModel dm = clazz.getAnnotation(DynamicModel.class);
				modelDescription.setName(dm.name());
				List<PropertyDescription> propertyDescriptionList = new ArrayList<PropertyDescription>();
				modelDescription
						.setPropertyDescriptionList(propertyDescriptionList);
				for (Method method : clazz.getMethods()) {
					if (method.isAnnotationPresent(DynamicProperty.class)) {
						DynamicProperty dp = method
								.getAnnotation(DynamicProperty.class);
						PropertyDescription propertyDescription = new PropertyDescription();
						propertyDescription.setName(dp.name());

						Control control = Control.decodeProperty(dp.control());
						propertyDescription.setControl(control);
						if (control.isSupportMutil() && control.isDynamic()) {
							Class<?> c = (Class<?>) ((ParameterizedType) method
									.getGenericReturnType())
									.getActualTypeArguments()[0];
							propertyDescription.setType(new CustomType(c
									.getName()));
						} else {
							propertyDescription.setType(new CustomType(method
									.getReturnType().getName()));
						}
						propertyDescriptionList.add(propertyDescription);
					}
				}
				add(modelDescription);
			}
		}
	}

	public void remove(ModelDescription modelDescription) {
		modelMapping.remove(modelDescription.getName());
	}

	public void update(ModelDescription oldOne, ModelDescription newOne) {
		modelMapping.remove(oldOne.getName());
		modelMapping.put(newOne.getName(), newOne);
	}

	public Collection<ModelDescription> getModelDescs() {
		return modelMapping.values();
	}

}

// $Id$