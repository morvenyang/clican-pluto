/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.support.spring;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Table;

/**
 * 该类主要是用来解决方便的配置各个pojo的schema
 * 
 * @author wei.zhang
 * 
 */
public class AnnotationSessionFactoryBean
		extends
		org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean {

	/**
	 * 默认的db schema
	 */
	private String defaultSchema;

	/**
	 * 对于默写表自定义其schema,其中key是table name,value是schema的集合
	 */
	private Map<String, String> customizedSchema;

	private Map<String, String> listeners;

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}

	public void setListeners(Map<String, String> listeners) {
		this.listeners = listeners;
	}

	public void setCustomizedSchema(Map<String, String> customizedSchema) {
		this.customizedSchema = new HashMap<String, String>();
		for (String schema : customizedSchema.keySet()) {
			String tableStr = customizedSchema.get(schema);
			String[] tables = tableStr.split(",");
			for (String table : tables) {
				this.customizedSchema.put(table.toUpperCase(), schema);
			}
		}
	}

	protected void postProcessMappings(Configuration config)
			throws HibernateException {
		super.postProcessMappings(config);
		if (listeners != null) {
			for (String eventType : listeners.keySet()) {
				config.setListener(eventType, listeners.get(eventType).trim());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void postProcessAnnotationConfiguration(
			AnnotationConfiguration config) throws HibernateException {
		if (customizedSchema == null) {
			return;
		}
		Map<String, String> schemaMapping = new HashMap<String, String>();
		Iterator<Table> it = (Iterator<Table>) config.getTableMappings();
		while (it.hasNext()) {
			Table table = it.next();
			String name = table.getName();
			if (customizedSchema.containsKey(name.toUpperCase())) {
				table.setSchema(customizedSchema.get(name.toUpperCase()));
				schemaMapping.put(name.toUpperCase(), customizedSchema.get(name.toUpperCase()));
			} else {
				if (StringUtils.isNotEmpty(defaultSchema)) {
					table.setSchema(defaultSchema);
					schemaMapping.put(name.toUpperCase(), defaultSchema);
				}
			}
		}
	}
}

// $Id$