/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.control;

import java.util.ArrayList;
import java.util.List;

import com.clican.pluto.common.type.NumberType;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.common.type.Type;

public class SelectOneControl extends MutilValueControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7093529107452112722L;

	@Override
	public String getName() {
		return "select one";
	}

	@Override
	public Control newControl() {
		return new SelectOneControl();
	}

	@Override
	public List<Type> getSupportTypeList() {
		List<Type> supportTypeList = new ArrayList<Type>();
		supportTypeList.add(new StringType());
		supportTypeList.add(new NumberType());
		return supportTypeList;
	}
}

// $Id$