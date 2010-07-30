/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.dpl.parser.impl.PagingParser;
import com.clican.pluto.dataprocess.dpl.parser.object.Pagination;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanA;

public class PagingParserTestCase extends BaseDplTestCase {

	private PagingParser pagingParser;

	private DplStatement dplStatement;

	public void setPagingParser(PagingParser pagingParser) {
		this.pagingParser = pagingParser;
	}

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void test1() throws Exception {
		String dpl = "select list.name from list offset 0 limit 1 reverse";
		Pagination pagination = pagingParser.parse(dpl, new ProcessorContextImpl());
		assertEquals(new Integer(0), pagination.getOffset());
		assertEquals(new Integer(1), pagination.getLimit());
		assertTrue(pagination.isReverse());
	}

	public void test2() throws Exception {
		String dpl = "select list.id from list offset 0 limit 1 reverse";
		Map<String, Object> map = getContext();
		List<Double> dList = dplStatement.execute(dpl, map, Double.class);
		assertEquals(1, dList.size());
	}

	public void test3() throws Exception {
		String dpl = "select list.id from list offset 0 limit 1";
		Map<String, Object> map = getContext();
		List<Double> dList = dplStatement.execute(dpl, map, Double.class);
		assertEquals(1, dList.size());
	}

	public void test4() throws Exception {
		String dpl = "select list.id from list limit 1";
		Map<String, Object> map = getContext();
		List<Double> dList = dplStatement.execute(dpl, map, Double.class);
		assertEquals(1, dList.size());
	}

	public void test5() throws Exception {
		String dpl = "select list.id from list limit 1 reverse";
		Map<String, Object> map = getContext();
		List<Double> dList = dplStatement.execute(dpl, map, Double.class);
		assertEquals(1, dList.size());
	}

	public void test6() throws Exception {
		String dpl = "select list.id from list offset 0";
		Map<String, Object> map = getContext();
		List<Double> dList = dplStatement.execute(dpl, map, Double.class);
		assertEquals(10, dList.size());
	}

	public void test7() throws Exception {
		String dpl = "select list.id as id,list.id as doubleValue from list offset 0 reverse";
		Map<String, Object> map = getContext();
		List<BeanA> dList = dplStatement.execute(dpl, map, BeanA.class);
		assertEquals(10, dList.size());
	}

	private Map<String, Object> getContext() throws Exception {
		List<BeanA> list = new ArrayList<BeanA>();
		for (int i = 0; i < 10; i++) {
			BeanA test = new BeanA();
			test.setId(i);
			list.add(test);
		}
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("list", list);
		return context;
	}
}

// $Id$