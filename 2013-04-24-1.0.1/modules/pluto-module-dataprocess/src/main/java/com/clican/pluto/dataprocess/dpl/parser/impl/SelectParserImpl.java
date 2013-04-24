/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.dpl.parser.FunctionParser;
import com.clican.pluto.dataprocess.dpl.parser.SelectParser;
import com.clican.pluto.dataprocess.dpl.parser.bean.Column;
import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.javacc.DplParserJavacc;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.dpl.parser.object.Select;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public class SelectParserImpl implements SelectParser {

	public final static String START_KEYWORD = "select";

	/**
	 * as前后必须带有空格，然后以','来分割的select部分
	 */

	private final static Set<String> END_KEYWORD = new HashSet<String>();

	public final static String AS_TOKEN = " as ";

	private FunctionParser functionParser;

	// 由于javacc的解析器比较难于实现parserSelect接口因此我们作为内置的处理
	// 通过修改spring配置可以简单的切换实现方式
	private boolean parseByJavacc = true;

	public void setFunctionParser(FunctionParser functionParser) {
		this.functionParser = functionParser;
	}

	public void setParseByJavacc(boolean parseByJavacc) {
		this.parseByJavacc = parseByJavacc;
	}

	static {
		END_KEYWORD.add(FromParserImpl.START_KEYWORD);
	}

	public Select parseSelect(String dpl, ProcessorContext context) throws DplParseException {
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
		if (parseByJavacc) {
			DplParserJavacc javacc = new DplParserJavacc(new ByteArrayInputStream((START_KEYWORD + " " + select).getBytes()));
			try {
				return javacc.SelectStatement();
			} catch (Exception e) {
				throw new DplParseException(e);
			}
		} else {
			int left = 0;
			int right = 0;
			int end = 0;
			int start = 0;
			List<String> selectList = new ArrayList<String>();
			while (end < select.length()) {
				String token = select.substring(end, end + 1);
				if (token.equals(FunctionParserImpl.START_KEYWORD)) {
					left++;
				} else if (token.equals(FunctionParserImpl.END_KEYWORD)) {
					right++;
				} else if (token.equals(FunctionParserImpl.PARAM_SPLIT_EXPR) && left == right) {
					selectList.add(select.substring(start, end).trim());
					start = end + 1;
				} else if (end == select.length() - 1) {
					selectList.add(select.substring(start, select.length()).trim());
				}
				end++;
			}
			List<Column> columnList = new ArrayList<Column>();
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
					Column col;
					PrefixAndSuffix pas;
					Function function = functionParser.parseFunction(functionDpl, context);
					if (function != null) {
						function.setExpr(functionDpl.trim());
						if (StringUtils.isNotEmpty(columnName)) {
							function.setColumnName(columnName);
						} else {
							function.setColumnName(function.getClass().getSimpleName());
						}
						pas = new PrefixAndSuffix(function);
					} else {
						if (StringUtils.isNotEmpty(columnName)) {
							pas = new PrefixAndSuffix(column.substring(0, column.indexOf(AS_TOKEN) + 1).trim());
						} else {
							pas = new PrefixAndSuffix(column);
							if (column.contains(".")) {
								columnName = column.substring(column.lastIndexOf(".") + 1);
							} else {
								columnName = column;
							}
							col = new Column(pas, columnName);
						}
					}
					col = new Column(pas, columnName);
					columnList.add(col);
				}
			} catch (Exception e) {
				throw new DplParseException(e);
			}
			return sel;
		}
	}
}

// $Id: SelectParser.java 13338 2010-05-27 08:22:07Z wei.zhang $