/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.dynamic.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.Launcher;

import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;
import com.clican.pluto.orm.dynamic.inter.IPojo;
import com.clican.pluto.orm.dynamic.inter.ITemplate;
import com.clican.pluto.orm.dynamic.inter.ITemplateModelRelation;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;

public class ClassLoaderUtilImpl implements ClassLoaderUtil {

	private final static Log log = LogFactory.getLog(ClassLoaderUtilImpl.class);

	private final static String SURE_FIRE_BOOTER_JAR = "surefirebooter";

	private DynamicClassLoader dynamicClassLoader;

	private ModelContainer modelContainer;

	public void setDynamicClassLoader(DynamicClassLoader dynamicClassLoader) {
		this.dynamicClassLoader = dynamicClassLoader;
	}

	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	public Set<String> getRuntimeJars() {
		Set<String> jars = new HashSet<String>();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (log.isDebugEnabled()) {
			log.debug("The ContextClassLoader[" + loader + "]");
		}
		populateJarsFromManifest(jars, loader);
		for (URL url : Launcher.getBootstrapClassPath().getURLs()) {
			jars.add(url.getPath());
		}
		populateJars(jars, loader);
		return jars;
	}

	private void populateJarsFromManifest(Set<String> jars, ClassLoader loader) {
		BufferedReader reader = null;
		try {
			Enumeration<URL> e = loader.getResources("META-INF/MANIFEST.MF");
			while (e.hasMoreElements()) {
				URL url = e.nextElement();
				if (url.getPath().contains(SURE_FIRE_BOOTER_JAR)) {
					reader = new BufferedReader(new InputStreamReader(url
							.openStream()));
					String line = null;
					StringBuffer content = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						content.append(line.trim());
					}
					String cont = content
							.toString()
							.replaceAll("Manifest-Version: 1.0Class-Path: ", "")
							.replaceAll(
									"Main-Class: org.apache.maven.surefire.booter.SurefireBooter",
									"");
					for (String file : cont.split(" ")) {
						if (file.contains("file")) {
							jars.add(file.replaceAll("file:", ""));
						}
					}
					break;
				}
			}
		} catch (IOException e) {
			log.error("", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}

		}
	}

	private void populateJars(Set<String> jars, ClassLoader loader) {
		if (!(loader instanceof URLClassLoader)) {
			if (log.isDebugEnabled()) {
				log.debug("The ClassLoader[" + loader.getClass().getName()
						+ "] is ignored");
			}
		} else {
			URLClassLoader urlClassLoader = (URLClassLoader) loader;

			for (URL url : urlClassLoader.getURLs()) {
				jars.add(url.getPath());
			}

		}
		if (loader == ClassLoader.getSystemClassLoader()) {
			return;
		} else {
			populateJars(jars, loader.getParent());
		}
	}

	public IDirectory newDirectory(IDirectory parent, String name) {
		IDirectory directory = null;
		try {
			directory = (IDirectory) (dynamicClassLoader
					.loadClass(Constants.DYNAMIC_MODEL_PACKAGE + "."
							+ Constants.DEFAULT_DIRECTORY_CLASS_NAME)
					.newInstance());
			String path = "/" + name;
			directory.setParent(parent);
			directory.setPath(path);
			directory.setName(name);
			directory.setChildren(new HashSet<IDirectory>());
		} catch (Exception e) {
			throw new PlutoException("The " + Constants.DYNAMIC_MODEL_PACKAGE
					+ "." + Constants.DEFAULT_DIRECTORY_CLASS_NAME
					+ " can't be found in the classloader.", e);
		}
		return directory;
	}

	public void configureTemplates(IDataModel dataModel,
			List<ITemplate> selectedTemplates) {
		ITemplateModelRelation relation = null;
		String name = Constants.DYNAMIC_MODEL_PACKAGE + "."
				+ Constants.DEFAULT_TEMPLATE_CLASS_NAME
				+ dataModel.getClass().getAnnotation(Entity.class).name()
				+ "Relation";
		Set<ITemplateModelRelation> set = new HashSet<ITemplateModelRelation>();
		try {
			for (ITemplate template : selectedTemplates) {
				relation = (ITemplateModelRelation) (dynamicClassLoader
						.loadClass(name).newInstance());
				relation.setDataModel(dataModel);
				relation.setTemplate(template);
				set.add(relation);
			}
			BeanUtils.setProperty(dataModel,
					Constants.DEFAULT_TEMPLATE_CLASS_NAME.toLowerCase()
							+ dataModel.getClass().getAnnotation(Entity.class)
									.name() + "RelationSet", set);
		} catch (Exception e) {
			throw new PlutoException("The " + name
					+ " can't be found in the classloader.", e);
		}
	}

	public IDataModel newDataModel(IDirectory parent,
			ModelDescription modelDescription) {
		IDataModel dataModel = null;
		try {
			dataModel = (IDataModel) (dynamicClassLoader
					.loadClass(Constants.DYNAMIC_MODEL_PACKAGE + "."
							+ modelDescription.getFirstCharUpperName())
					.newInstance());
			dataModel.setParent(parent);
		} catch (Exception e) {
			throw new PlutoException("The " + Constants.DYNAMIC_MODEL_PACKAGE
					+ "." + modelDescription.getFirstCharUpperName()
					+ " can't be found in the classloader.", e);
		}
		return dataModel;
	}

	public ITemplate newTemplate() {
		ITemplate template = null;
		try {
			template = (ITemplate) (dynamicClassLoader
					.loadClass(Constants.DYNAMIC_MODEL_PACKAGE + ".Template")
					.newInstance());
		} catch (Exception e) {
			throw new PlutoException("The " + Constants.DYNAMIC_MODEL_PACKAGE
					+ ".Template" + " can't be found in the classloader.", e);
		}
		return template;
	}

	public List<Class<?>> getAllDataModelClass() {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (ModelDescription modelDescription : modelContainer.getModelDescs()) {
			try {
				result.add(dynamicClassLoader
						.loadClass(Constants.DYNAMIC_MODEL_PACKAGE + "."
								+ modelDescription.getFirstCharUpperName()));
			} catch (Exception e) {
				throw new PlutoException("The "
						+ Constants.DYNAMIC_MODEL_PACKAGE + "."
						+ modelDescription.getFirstCharUpperName()
						+ " can't be found in the classloader.", e);
			}
		}
		return result;
	}

	public Class<?> getClass(IPojo pojo) {
		String modelClass = Constants.DYNAMIC_MODEL_PACKAGE + "."
				+ pojo.getClass().getAnnotation(Entity.class).name();
		return this.getClass(modelClass);
	}

	public Class<?> getClass(String modelClass) {
		Class<?> clazz = null;
		try {
			clazz = dynamicClassLoader.loadClass(modelClass);
		} catch (Exception e) {
			throw new PlutoException("The " + modelClass
					+ " can't be found in the classloader.", e);
		}
		return clazz;
	}

	public Class<?> getClass(ModelDescription modelDescription) {
		return this.getClass(Constants.DYNAMIC_MODEL_PACKAGE + "."
				+ modelDescription.getFirstCharUpperName());
	}

}

// $Id: ClassLoaderUtilImpl.java 556 2009-06-16 04:30:01Z clican $
