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

public class InputTextAreaControl extends Control {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2041796748503181398L;

	@Override
	public String getName() {
		return "input text area";
	}

	@Override
	public Control newControl() {
		return new InputTextAreaControl();
	}

	@Override
	public List<Type> getSupportTypeList() {
		List<Type> supportTypeList = new ArrayList<Type>();
		supportTypeList.add(new StringType());
		supportTypeList.add(new NumberType());
		return supportTypeList;
	}

	
}


//$Id$