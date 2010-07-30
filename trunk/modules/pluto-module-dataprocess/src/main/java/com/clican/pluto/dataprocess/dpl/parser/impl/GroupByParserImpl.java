/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clican.pluto.dataprocess.dpl.function.MultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.SingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.FunctionParser;
import com.clican.pluto.dataprocess.dpl.parser.GroupByParser;
import com.clican.pluto.dataprocess.dpl.parser.bean.Group;
import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.dpl.parser.object.GroupBy;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public class GroupByParserImpl implements GroupByParser {

	public final static String START_KEYWORD = "group by";

	private final static Set<String> END_KEYWORD = new HashSet<String>();

	static {
		END_KEYWORD.add(OrderByParserImpl.START_KEYWORD);
	}

	private FunctionParser functionParser;

	public void setFunctionParser(FunctionParser functionParser) {
		this.functionParser = functionParser;
	}

	public GroupBy parse(String dpl, ProcessorContext context)
			throws DplParseException {
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
		String group = dpl.substring(index, length);

		int i = 0;
		int left = 0;
		int right = 0;
		int lastI = 0;
		List<Group> groups = new ArrayList<Group>();
		while (i < group.length()) {
			String p = group.substring(i, i + 1);
			if ((p.equals(",") && left == right) || i == group.length() - 1) {
				Group gro = new Group();
				String g;
				if (i == group.length() - 1) {
					g = group.substring(lastI).trim();
				} else {
					g = group.substring(lastI, i).trim();
				}

				Function function = functionParser.parse(g, context);
				if (function != null) {
					function.setExpr(g);
					if (function instanceof MultiRowFunction) {
						throw new DplParseException("在where条件中不支持多行处理函数");
					}
					gro.setFunction((SingleRowFunction) function);
				}
				gro.setExpr(g);
				groups.add(gro);
				lastI = i + 1;
			}
			if (p.equals(FunctionParserImpl.START_KEYWORD)) {
				left++;
			} else if (p.equals(FunctionParserImpl.END_KEYWORD)) {
				right++;
			}

			i++;
		}

		GroupBy groupBy = new GroupBy(groups);
		return groupBy;
	}

}

// $Id: GroupByParser.java 12410 2010-05-13 06:55:57Z wei.zhang $