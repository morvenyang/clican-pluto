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

import com.clican.pluto.common.type.StringArrayType;
import com.clican.pluto.common.type.Type;

public class SelectMutilControl extends MutilValueControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8841674154476925724L;

	@Override
	public String getName() {
		return "select mutil";
	}
	
	@Override
	public boolean isSupportMutil() {
		return true;
	}

	@Override
	public List<Type> getSupportTypeList() {
		List<Type> supportTypeList = new ArrayList<Type>();
		supportTypeList.add(new StringArrayType());
		return supportTypeList;
	}

	@Override
	public Control newControl() {
		return new SelectMutilControl();
	}

}

// $Id$