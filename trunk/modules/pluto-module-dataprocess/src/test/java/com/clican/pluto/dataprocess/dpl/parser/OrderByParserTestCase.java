/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import java.util.ArrayList;
import java.util.List;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.dpl.parser.impl.OrderByParser;
import com.clican.pluto.dataprocess.dpl.parser.object.OrderBy;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanA;

public class OrderByParserTestCase extends BaseDplTestCase {

	private OrderByParser orderByParser;

	private DplStatement dplStatement;

	public void setOrderByParser(OrderByParser orderByParser) {
		this.orderByParser = orderByParser;
	}

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testOrderBy1() throws Exception {
		String dpl = "select sum(list.id) as sumId,list.name as name from list order by name";
		OrderBy orderBy = orderByParser.parse(dpl, new ProcessorContextImpl());
		assertTrue(orderBy.getOrders().size() == 1);
	}

	public void testOrderBy2() throws Exception {
		String dpl = "select sum(list.id) as sumId,list.name as name from list order by name asc,password desc";
		OrderBy orderBy = orderByParser.parse(dpl, new ProcessorContextImpl());
		assertTrue(orderBy.getOrders().size() == 2);
	}

	public void testOrderBy3() throws Exception {
		String dpl = "select list.* as list from list order by list.name asc";
		List<BeanA> result = dplStatement.execute(dpl, getContext(), BeanA.class);
		assertEquals(10, result.size());
	}

	private ProcessorContext getContext() throws Exception {
		List<BeanA> list = new ArrayList<BeanA>();
		for (int i = 0; i < 10; i++) {
			BeanA test = new BeanA();
			test.setId(i);
			test.setName("" + i/2);
			list.add(test);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list", list);
		return context;
	}
}

// $Id$