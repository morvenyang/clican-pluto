/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.dynamic.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.orm.dynamic.inter.IDirectory;

/**
 * This class extends <code>URLClassLoader</code>. It can be used to dynamically
 * load class and override exist class with new definition. The caller must
 * replace the
 * <code>Thread.currentThread().setContextClassLoader(ClassLoader classLoader)</code>
 * with this class.
 * <p>
 * Because the Hibernate will use the
 * <code>Thread.currentThread().getContextClassLoader()</code> as the class
 * loader for POJO loading.
 * <p>
 * For the Spring we also need set the beanClassLoader for
 * LocalSessionFactoryBean to replace the default ResourceClassLoader.
 * 
 * For details, please refer to <code>LocalWrapSessionFactoryBean</code>
 * 
 * @since 0.0.1
 * @author clican
 * 
 */
public class DynamicClassLoader extends ClassLoader {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(DynamicClassLoader.class);

	private String tempORMCfgPojoFolder;

	private URLClassLoader urlClassLoader;

	private Map<String, Class<?>> loadedDynamicClasses = new HashMap<String, Class<?>>();

	private ClassLoader parent;

	public DynamicClassLoader() {
		this.parent = Thread.currentThread().getContextClassLoader();
		log.debug(parent.toString());
		try {
			this.parent.loadClass(IDirectory.class.getName());
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public void setTempORMCfgPojoFolder(String tempORMCfgPojoFolder) {
		this.tempORMCfgPojoFolder = tempORMCfgPojoFolder;
	}

	public Collection<Class<?>> getLoadedDynamicClasses() {
		return loadedDynamicClasses.values();
	}

	/**
	 * This will be initialize by Spring Framework.
	 * 
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 */
	public void init() throws MalformedURLException, ClassNotFoundException {
		File file = new File(tempORMCfgPojoFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		urlClassLoader = new URLClassLoader(new URL[] { file.toURI().toURL() },
				parent);
		loadAllClasses(file);
	}

	private void loadAllClasses(File dir) throws ClassNotFoundException {
		if (dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				loadAllClasses(file);
			}
		} else {
			if (dir.getName().contains("class")) {
				String clazz = dir.getAbsolutePath().replaceAll("\\\\", "/")
						.replaceAll(
								new File(tempORMCfgPojoFolder)
										.getAbsolutePath().replaceAll("\\\\",
												"/")
										+ "/", "").replaceAll("/", "\\.")
						.replaceAll("\\.class", "");
				if (log.isDebugEnabled()) {
					log.debug("class=" + clazz);
				}
				Class<?> c = urlClassLoader.loadClass(clazz);
				loadedDynamicClasses.put(c.getName(), c);
			}
		}
	}

	public synchronized void refreshClasses() throws ClassNotFoundException {
		URL[] urls = urlClassLoader.getURLs();
		urlClassLoader = new URLClassLoader(urls, urlClassLoader.getParent());
		File file = new File(tempORMCfgPojoFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		loadedDynamicClasses.clear();
		loadAllClasses(file);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (loadedDynamicClasses.containsKey(name)) {
			return loadedDynamicClasses.get(name);
		} else {
			return urlClassLoader.loadClass(name);
		}
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		if (urlClassLoader == null) {
			return super.findResources(name);
		} else {
			return urlClassLoader.findResources(name);
		}

	}

}

// $Id: DynamicClassLoader.java 700 2010-02-10 02:50:12Z clican $
