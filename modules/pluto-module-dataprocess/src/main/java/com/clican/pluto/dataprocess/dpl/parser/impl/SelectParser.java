/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.dpl.parser.DplParser;
import com.clican.pluto.dataprocess.dpl.parser.bean.Column;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.dpl.parser.object.Select;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public class SelectParser implements DplParser {

	public final static String START_KEYWORD = "select";

	/**
	 * as前后必须带有空格，然后以','来分割的select部分
	 */

	private final static Set<String> END_KEYWORD = new HashSet<String>();

	public final static String AS_TOKEN = " as ";

	private FunctionParser functionParser;

	public void setFunctionParser(FunctionParser functionParser) {
		this.functionParser = functionParser;
	}

	static {
		END_KEYWORD.add(FromParser.START_KEYWORD);
	}

	public Select parse(String dpl, ProcessorContext context) throws DplParseException {
		int index = dpl.indexOf(START_KEYWORD);
		if (index < 0) {
			return null;
		}
		int length = dpl.length();
		for (String endKeyword : END_KEYWORD) {
			int temp = dpl.indexOf(endKeyword);
			if (temp != -1 && temp > index && temp < length) {
				length = temp;
			}
		}
		index = index + START_KEYWORD.length();
		String select = dpl.substring(index, length);
		int left = 0;
		int right = 0;
		int end = 0;
		int start = 0;
		List<String> selectList = new ArrayList<String>();
		while (end < select.length()) {
			String token = select.substring(end, end + 1);
			if (token.equals(FunctionParser.START_KEYWORD)) {
				left++;
			} else if (token.equals(FunctionParser.END_KEYWORD)) {
				right++;
			} else if (token.equals(FunctionParser.PARAM_SPLIT_EXPR) && left == right) {
				selectList.add(select.substring(start, end).trim());
				start = end + 1;
			} else if (end == select.length() - 1) {
				selectList.add(select.substring(start, select.length()).trim());
			}
			end++;
		}
		List<Object> columnList = new ArrayList<Object>();
		Select sel = new Select(columnList);
		try {
			for (int i = 0; i < selectList.size(); i++) {
				String column = selectList.get(i).trim();
				String columnName = null;
				String functionDpl = null;
				if (column.contains(AS_TOKEN)) {
					columnName = column.substring(column.indexOf(AS_TOKEN) + AS_TOKEN.length(), column.length()).trim();
					functionDpl = column.substring(0, column.indexOf(AS_TOKEN));
				} else {
					functionDpl = column;
				}
				if (functionParser.containFunction(functionDpl)) {
					Function function = functionParser.parse(functionDpl, context);
					function.setExpr(functionDpl.trim());
					if (StringUtils.isNotEmpty(columnName)) {
						function.setColumnName(columnName);
					} else {
						function.setColumnName(function.getClass().getSimpleName());
					}
					columnList.add(function);
				} else {
					Column col = new Column();
					if (StringUtils.isNotEmpty(columnName)) {
						col.setColumnName(columnName);
						PrefixAndSuffix prefixAndSuffix = new PrefixAndSuffix(column.substring(0, column.indexOf(AS_TOKEN) + 1).trim(), context);
						col.setPrefixAndSuffix(prefixAndSuffix);
					} else {
						if (column.contains(".")) {
							col.setColumnName(column.substring(column.lastIndexOf(".") + 1));
						} else {
							col.setColumnName(column);
						}
						PrefixAndSuffix prefixAndSuffix = new PrefixAndSuffix(column, context);
						col.setPrefixAndSuffix(prefixAndSuffix);
					}
					columnList.add(col);
				}
			}
		} catch (Exception e) {
			throw new DplParseException(e);
		}
		return sel;
	}
}

// $Id: SelectParser.java 13338 2010-05-27 08:22:07Z wei.zhang $