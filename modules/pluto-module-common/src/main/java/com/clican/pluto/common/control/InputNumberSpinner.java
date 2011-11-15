/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.common.control;

import java.util.ArrayList;
import java.util.List;

import com.clican.pluto.common.type.DoubleType;
import com.clican.pluto.common.type.FloatType;
import com.clican.pluto.common.type.IntegerType;
import com.clican.pluto.common.type.LongType;
import com.clican.pluto.common.type.Type;

public class InputNumberSpinner extends Control {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5650805149006605814L;

	@Override
	public String getName() {
		return "input number spinner";
	}

	@Override
	public Control newControl() {
		return new InputNumberSpinner();
	}

	@Override
	public List<Type> getSupportTypeList() {
		List<Type> supportTypeList = new ArrayList<Type>();
		supportTypeList.add(new LongType());
		supportTypeList.add(new IntegerType());
		supportTypeList.add(new FloatType());
		supportTypeList.add(new DoubleType());
		return supportTypeList;
	}

}


//$Id$