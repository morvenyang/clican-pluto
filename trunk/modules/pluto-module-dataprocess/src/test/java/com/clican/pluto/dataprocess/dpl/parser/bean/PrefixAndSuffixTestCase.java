/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;
import com.clican.pluto.dataprocess.testbean.BeanA;

public class PrefixAndSuffixTestCase extends TestCase {

	public void test1() throws Exception {
		List<String> varNames = new ArrayList<String>();
		varNames.add("list");
		ProcessorContext context = getContext();
		List<BeanA> list = context.getAttribute("list");
		PrefixAndSuffix pas1 = new PrefixAndSuffix("list.id", getContext());
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("list", list.get(0));
		assertEquals(new Integer(0), (Integer) pas1.getValue(row));
		PrefixAndSuffix pas2 = new PrefixAndSuffix("dual.id", getContext());
		assertEquals(new Integer(1), pas2.getConstantsValue(Integer.class));
		PrefixAndSuffix pas3 = new PrefixAndSuffix("dual.beanA.id", getContext());
		assertEquals(new Double(1), pas3.getConstantsValue(Double.class));
		assertEquals("1", pas3.getConstantsValue(String.class));
		PrefixAndSuffix pas4 = new PrefixAndSuffix("dual.beanA.name", getContext());
		assertEquals(new Double(1), pas4.getConstantsValue(Double.class));
		List<Map<String, Object>> rowSet = new ArrayList<Map<String, Object>>();
		Map<String, Object> row1 = new HashMap<String, Object>();
		row1.put("list", list.get(1));
		rowSet.add(row);
		rowSet.add(row1);
		List<Integer> result = pas1.getValues(rowSet);
		assertEquals(2, result.size());
	}

	public void test2() throws Exception {
		List<String> varNames = new ArrayList<String>();
		varNames.add("list");
		ProcessorContext context = getContext();
		List<BeanA> list = context.getAttribute("list");
		PrefixAndSuffix pas1 = new PrefixAndSuffix("list.id", getContext());
		Map<String, Object> row = new HashMap<String, Object>();
		row.put("list", list.get(0));
		try {
			pas1.isSupportInMultiFunctionWithoutGroupBy();
			fail("应该提示,在有多行处理函数并且没有分组的情况下,不支持普通列的查询");
		} catch (PrefixAndSuffixException e) {

		}

	}

	private ProcessorContext getContext() throws Exception {
		List<BeanA> list = new ArrayList<BeanA>();
		for (int i = 0; i < 10; i++) {
			BeanA test = new BeanA();
			test.setId(i);
			test.setName("" + i);
			list.add(test);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list", list);
		context.setAttribute("id", 1);
		BeanA beanA = new BeanA();
		beanA.setId(1);
		beanA.setName("1");
		context.setAttribute("beanA", beanA);
		return context;
	}
}

// $Id$