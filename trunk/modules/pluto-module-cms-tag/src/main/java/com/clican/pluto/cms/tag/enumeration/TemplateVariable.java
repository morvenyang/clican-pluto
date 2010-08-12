/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cms.tag.enumeration;

import org.apache.commons.lang.StringUtils;

public enum TemplateVariable {

	/**
	 * The path of current directory.
	 */
	PATH("path");

	private String variable;

	private TemplateVariable(String variable) {
		this.variable = variable;
	}

	public String variable() {
		return variable;
	}

	public static TemplateVariable convert(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		for (TemplateVariable tv : values()) {
			if (tv.variable.equals(name)) {
				return tv;
			}
		}
		return null;
	}
}

// $Id$