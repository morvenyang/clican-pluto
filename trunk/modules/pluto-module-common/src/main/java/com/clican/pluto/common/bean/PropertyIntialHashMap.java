/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class PropertyIntialHashMap<K, V> extends HashMap<K, V> implements
		ApplicationContextAware {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3312811089454335428L;

	private String propertyStr;

	private ApplicationContext context;

	public PropertyIntialHashMap() {

	}

	public PropertyIntialHashMap(String propertyStr) {
		if (StringUtils.isEmpty(propertyStr)) {
			throw new IllegalArgumentException(
					"The initial property must like 'a'=>{'a1','a2','a3'}; 'b'=>{'b1','b2'}; 'c'=>{}; d=>{d1,d2,d3}; 'e'=>'e1'; f=f1. the value is in '' will be convert to string, the value is not in '' will be convert as a spring bean name");
		}
		this.propertyStr = propertyStr;
	}

	public String getPropertyStr() {
		return propertyStr;
	}

	public void setPropertyStr(String propertyStr) {
		if (StringUtils.isEmpty(propertyStr)) {
			throw new IllegalArgumentException(
					"The initial property must like 'a'=>{'a1','a2','a3'}; 'b'=>{'b1','b2'}; 'c'=>{}; d=>{d1,d2,d3}; 'e'=>'e1'; f=f1. the value is in '' will be convert to string, the value is not in '' will be convert as a spring bean name");
		}
		this.propertyStr = propertyStr;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public void initProperty() {

		String[] entries = propertyStr.split(";");
		for (String entry : entries) {
			try {
				String[] str = entry.split("=>");
				Object key = str[0].trim();
				if (((String) key).indexOf("'") == -1) {
					key = context.getBean((String) key);
				} else {
					key = ((String) key).replaceAll("\\'", "").trim();
				}
				Object value = null;
				if (str[1].indexOf("{") != -1 && str[1].indexOf("}") != -1) {
					value = new ArrayList();
					String[] values = str[1].replaceAll("\\{", "").replaceAll(
							"\\}", "").split(",");
					for (String v : values) {
						v = v.trim();
						if (v.indexOf("'") == -1) {
							if (StringUtils.isNumeric(v)) {
								((List) value).add(new Long(v));
							}
							((List) value).add(context.getBean(v));
						} else {
							v = v.replaceAll("\\'", "").trim();
							((List) value).add(v);
						}
					}
				} else {
					value = str[1].trim();
					if (((String) value).indexOf("'") == -1) {
						if (StringUtils.isNumeric(value.toString())) {
							((List) value).add(new Long(value.toString()));
						}
						value = context.getBean((String) value);
					} else {
						value = ((String) value).replaceAll("\\'", "").trim();
					}
				}
				put((K) key, (V) value);
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Initial property error,["
								+ entry
								+ "]The initial property must like 'a'=>{'a1','a2','a3'}, 'b'=>{'b1','b2'}, 'c'=>{}, d=>{d1,d2,d3}, 'e'=>'e1', f=f1. the value is in '' will be convert to string, the value is not in '' will be convert as a spring bean name",
						e);
			}
		}
	}
}

// $Id$