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
import java.util.Map;
import java.util.Set;

import com.clican.pluto.dataprocess.dpl.parser.DplParser;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 对From关键字进行解析
 *
 * @author wei.zhang
 *
 */
public class FromParser implements DplParser {
	/**
	 * 解析的开始位置
	 */
	public final static String START_KEYWORD = "from";
	
	public final static String CONSTANTS_KEY = "dual";
	/**
	 * 解析的结束位置
	 */
	private final static Set<String> END_KEYWORD = new HashSet<String>();

	static {
		END_KEYWORD.add(FilterParser.START_KEYWORD);
		END_KEYWORD.add(GroupByParser.START_KEYWORD);
		END_KEYWORD.add(OrderByParser.START_KEYWORD);
		END_KEYWORD.add(PagingParser.START_KEYWORD1);
		END_KEYWORD.add(PagingParser.START_KEYWORD2);
		END_KEYWORD.add(PagingParser.START_KEYWORD3);
	}

	@Override
	public From parse(String dpl, ProcessorContext context, Map<String, Object> parseContext) throws DplParseException {
		int index = dpl.indexOf(START_KEYWORD);
		if (index < 0) {
			return null;
		}
		int length = dpl.length();
		for (String endKeyword : END_KEYWORD) {
			int temp = dpl.indexOf(endKeyword);
			if (temp != -1 && temp < length) {
				length = temp;
			}
		}
		index = index + START_KEYWORD.length();
		String from = dpl.substring(index, length);
		String[] temp = from.split(",");
		List<String> froms = new ArrayList<String>();
		for (String t : temp) {
			froms.add(t.trim());
		}
		From f = new From(froms);
		parseContext.put(START_KEYWORD, f);
		return f;
	}
}

// $Id: FromParser.java 16149 2010-07-15 02:58:30Z wei.zhang $