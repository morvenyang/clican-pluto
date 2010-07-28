/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.support.spring;

import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.clican.pluto.common.util.PropertyUtilS;

public class MapAndNestedPropertySqlParameterSource extends MapSqlParameterSource {

	@SuppressWarnings("unchecked")
	public MapAndNestedPropertySqlParameterSource(Map values) {
		addValues(values);
	}

	@Override
	public boolean hasValue(String paramName) {
		try {
			Object obj = PropertyUtilS.getNestedProperty(this.getValues(), paramName);
			return obj != null;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public Object getValue(String paramName) {
		try {
			Object obj = PropertyUtilS.getNestedProperty(this.getValues(), paramName);
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

// $Id$