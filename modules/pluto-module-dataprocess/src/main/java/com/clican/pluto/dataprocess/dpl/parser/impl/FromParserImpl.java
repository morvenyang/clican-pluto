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

import com.clican.pluto.dataprocess.dpl.parser.FromParser;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 对From关键字进行解析
 *
 * @author clican
 *
 */
public class FromParserImpl implements FromParser {
	
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
		END_KEYWORD.add(FilterParserImpl.START_KEYWORD);
		END_KEYWORD.add(GroupByParserImpl.START_KEYWORD);
		END_KEYWORD.add(OrderByParserImpl.START_KEYWORD);
		END_KEYWORD.add(PagingParserImpl.START_KEYWORD1);
		END_KEYWORD.add(PagingParserImpl.START_KEYWORD2);
		END_KEYWORD.add(PagingParserImpl.START_KEYWORD3);
	}
	
	
	public From parseFrom(String dpl, ProcessorContext context) throws DplParseException {
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
		return f;
	}
}

// $Id: FromParser.java 16149 2010-07-15 02:58:30Z wei.zhang $