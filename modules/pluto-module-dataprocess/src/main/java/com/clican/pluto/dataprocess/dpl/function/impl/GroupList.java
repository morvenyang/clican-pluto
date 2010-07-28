/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

public class GroupList extends BaseMultiRowFunction {

	private PrefixAndSuffix listName;

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		if (listName == null) {
			return rowSet;
		} else {
			List<Object> result = new ArrayList<Object>();
			for (Map<String, Object> row : rowSet) {
				result.add(listName.getValue(row));
			}
			return result;
		}
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		// TODO Auto-generated method stub
		super.setParams(params, from, context);
		if (this.pasList.size() > 0) {
			listName = pasList.get(0);
		}
	}

}

// $Id: GroupList.java 12410 2010-05-13 06:55:57Z wei.zhang $