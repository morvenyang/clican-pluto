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
import com.clican.pluto.common.type.Type;

public class SingalCheckBoxControl extends MutilValueControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7275588389430178815L;

	@Override
	public String getName() {
		return "singal check box";
	}

	@Override
	public Control newControl() {
		return new SingalCheckBoxControl();
	}

	@Override
	public List<Type> getSupportTypeList() {
		List<Type> supportTypeList = new ArrayList<Type>();
		supportTypeList.add(new NumberType());
		return supportTypeList;
	}
}

// $Id$