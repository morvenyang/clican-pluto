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

public class MutilCheckBoxControl extends MutilValueControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4064301538616937217L;

	@Override
	public boolean isSupportMutil() {
		return true;
	}

	@Override
	public String getName() {
		return "mutil check box";
	}

	@Override
	public Control newControl() {
		return new MutilCheckBoxControl();
	}

	@Override
	public List<Type> getSupportTypeList() {
		List<Type> supportTypeList = new ArrayList<Type>();
		supportTypeList.add(new StringArrayType());
		return supportTypeList;
	}
}


//$Id$