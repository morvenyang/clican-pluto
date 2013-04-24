/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.common.inter.CloneBean;
import com.clican.pluto.common.util.StringUtils;

public abstract class Type implements Serializable, CloneBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2279749345040330546L;

	public abstract String getName();

	public abstract String getClassName();

	public abstract String getDeclareString();

	public static Type decodeProperty(String description) {
		Map<String, String> properties = new HashMap<String, String>();
		for (String s : description.split(";")) {
			String[] entry = s.split("=");
			if (entry.length == 1) {
				properties.put(entry[0], null);
			} else {
				properties.put(entry[0], entry[1]);
			}
		}
		Type tepe = null;
		if (properties.get("class") != null) {
			try {
				tepe = (Type) Class.forName(properties.get("class"))
						.newInstance();
			} catch (Exception e) {
				throw new PlutoException(e);
			}
		} else {
			throw new PlutoException(
					"Cannot get class property from dymanic property's control annotation.");
		}
		properties.remove("class");
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
				continue;
			}
			try {
				org.apache.commons.beanutils.BeanUtils.setProperty(tepe, key,
						value);
			} catch (Exception e) {
				List<String> listValue = StringUtils
						.getListFromSymbolSplitString(value, ",");
				try {
					org.apache.commons.beanutils.BeanUtils.setProperty(tepe,
							key, listValue);
				} catch (Exception ex) {
					throw new PlutoException(ex);
				}
			}
		}
		return tepe;
	}

	public abstract String codecProperty();

	public Type getCloneBean() {
		return this;
	}
}

// $Id$