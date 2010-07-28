/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.dpl.parser.impl.FromParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.GroupByParser;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.dpl.parser.object.GroupBy;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanA;

public class GroupByParserTestCase extends BaseDplTestCase {

	private GroupByParser groupByParser;

	private DplStatement dplStatement;

	public void setGroupByParser(GroupByParser groupByParser) {
		this.groupByParser = groupByParser;
	}

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testGroupBy1() throws Exception {
		String dpl = "select sum(list.id) as sumId,list.name as name from list group by list.name";
		GroupBy groupBy = groupByParser.parse(dpl, new ProcessorContextImpl(), new HashMap<String, Object>());
		assertTrue(groupBy.getGroups().size() == 1);
	}

	public void testGroupBy2() throws Exception {
		String dpl = "select sum(list.id) as sumId,list.name as name from list group by list.name,list.password";
		GroupBy groupBy = groupByParser.parse(dpl, new ProcessorContextImpl(), new HashMap<String, Object>());
		assertTrue(groupBy.getGroups().size() == 2);
	}

	public void testGroupBy3() throws Exception {
		String dpl = "select sum(list.id) as sumId,toChar(list.publishDate,'yyyyMMdd') as name from list group by list.name,toChar(list.publishDate,'yyyyMMdd')";
		Map<String, Object> parseContext = new HashMap<String, Object>();
		List<String> froms = new ArrayList<String>();
		froms.add("list");
		From from = new From(froms);
		parseContext.put(FromParser.START_KEYWORD, from);
		GroupBy groupBy = groupByParser.parse(dpl, new ProcessorContextImpl(), parseContext);
		assertTrue(groupBy.getGroups().size() == 2);
	}

	public void testGroupBy4() throws Exception {
		String dpl = "select sum(list.id) as sumId,toChar(list.date,'yyyyMMdd') as date from list group by toChar(list.date,'yyyyMMdd')";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}

	public void testGroupBy5() throws Exception {
		String dpl = "select sum(list.id) as sumId,toChar(list.date,'yyyyMMdd') as date from list group by toChar(list.date1,'yyyyMMdd')";
		try {
			dplStatement.execute(dpl, getContext());
			fail("应该group by出错");
		} catch (Exception e) {

		}
	}

	public void testGroupBy6() throws Exception {
		String dpl = "select sum(list.id) as sumId from list group by sum(list.id)";
		try {
			dplStatement.execute(dpl, getContext());
			fail("应该group by出错");
		} catch (Exception e) {

		}
	}

	public void testGroupBy7() throws Exception {
		String dpl = "select sum(list.id) as sumId from list group by dual.id";
		try {
			dplStatement.execute(dpl, getContext());
			fail("应该group by出错");
		} catch (Exception e) {

		}
	}

	private ProcessorContext getContext() throws Exception {
		List<BeanA> list = new ArrayList<BeanA>();
		for (int i = 0; i < 10; i++) {
			BeanA test = new BeanA();
			test.setId(i);
			test.setDate(new Date());
			list.add(test);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list", list);
		context.setAttribute("id", 1);
		return context;
	}
}

// $Id$