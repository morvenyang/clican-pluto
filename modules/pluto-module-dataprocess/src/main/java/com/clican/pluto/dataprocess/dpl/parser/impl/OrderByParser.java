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

import com.clican.pluto.dataprocess.dpl.parser.DplParser;
import com.clican.pluto.dataprocess.dpl.parser.bean.Order;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.OrderDir;
import com.clican.pluto.dataprocess.dpl.parser.object.OrderBy;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public class OrderByParser implements DplParser {

	public static final String START_KEYWORD = "order by";

	private final static Set<String> END_KEYWORD = new HashSet<String>();

	static {
		END_KEYWORD.add(GroupByParser.START_KEYWORD);
		END_KEYWORD.add(PagingParser.START_KEYWORD1);
		END_KEYWORD.add(PagingParser.START_KEYWORD2);
	}

	
	public OrderBy parse(String dpl, ProcessorContext context) throws DplParseException {
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
		String order = dpl.substring(index, length);
		String[] temp = order.split(",");
		List<Order> orders = new ArrayList<Order>();
		for (String t : temp) {
			t = t.trim();
			String[] exprs = t.split(" ");
			Order ord = new Order();
			if (exprs.length == 1) {
				ord.setExpr(exprs[0]);
			} else if (exprs.length <= 2) {
				ord.setExpr(exprs[0]);
				ord.setOrderDir(OrderDir.convert(exprs[1]));
				if (ord.getOrderDir() == null) {
					throw new DplParseException("order by解析错误");
				}
			} else {
				throw new DplParseException("order by解析错误");
			}
			orders.add(ord);
		}
		OrderBy orderBy = new OrderBy(orders);
		return orderBy;
	}

}

// $Id: OrderByParser.java 12670 2010-05-18 09:23:05Z wei.zhang $