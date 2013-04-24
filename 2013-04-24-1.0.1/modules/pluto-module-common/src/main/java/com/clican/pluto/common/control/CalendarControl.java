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

import com.clican.pluto.common.type.CalendarType;
import com.clican.pluto.common.type.DateType;
import com.clican.pluto.common.type.Type;

public class CalendarControl extends Control {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8033464642225370220L;

	@Override
	public String getName() {
		return "calendar";
	}

	@Override
	public Control newControl() {
		return new CalendarControl();
	}

	@Override
	public List<Type> getSupportTypeList() {
		List<Type> supportTypeList = new ArrayList<Type>();
		supportTypeList.add(new CalendarType());
		supportTypeList.add(new DateType());
		return supportTypeList;
	}

}

// $Id$