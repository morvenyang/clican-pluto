/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.exception.DplException;
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class NestedFunctionTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testSingleAndSingleNestedFunctionInSelect() throws Exception {
		List<Date> result = dplStatement.execute("select toDate(toChar(price.date)) as date from price", getContext(), Date.class);
		assertEquals(10, result.size());
		assertTrue(result.get(0) instanceof Date);
	}

	public void testSingleAndMultiNestedFunctionInSelect() throws Exception {
		List<String> result = dplStatement.execute("select toChar(max(price.date)) as date from price", getContext(), String.class);
		assertEquals(1, result.size());
		assertTrue(result.get(0) instanceof String);
	}

	public void testMultiAndSingleNestedFunctionInSelect() throws Exception {
		List<String> result = dplStatement.execute("select max(toChar(price.date)) as date from price", getContext(), String.class);
		assertEquals(1, result.size());
		assertTrue(result.get(0) instanceof String);
	}

	public void testMultiAndMultiNestedFunctionInSelect() throws Exception {
		DplException e = null;
		try {
			dplStatement.execute("select max(max(price.date)) as date from price", getContext(), String.class);
		} catch (DplException ex) {
			e = ex;
		}
		assertNotNull(e);
	}

	public void testSingleAndSingleNestedFunctionInGroup() throws Exception {
		List<Date> result = dplStatement.execute("select toDate(toChar(price.date)) as date from price group by price.date", getContext(), Date.class);
		assertEquals(5, result.size());
		assertTrue(result.get(0) instanceof Date);
	}

	public void testSingleAndMultiNestedFunctionInGroup() throws Exception {
		List<Map<String, Object>> result = dplStatement.execute(
				"select toChar(max(price.date2),'yyyyMMdd HH:mm:ss') as date,price.date as t from price group by price.date", getContext());
		assertEquals(5, result.size());
		assertEquals("20100201 00:00:00", result.get(0).get("date"));
		assertEquals("20100501 00:00:00", result.get(1).get("date"));
		assertEquals("20100603 00:00:00", result.get(2).get("date"));
		assertEquals("20010204 00:00:00", result.get(3).get("date"));
		assertEquals("20110405 00:00:00", result.get(4).get("date"));
	}

	public void testMultiAndSingleNestedFunctionInGroup() throws Exception {
		List<Map<String, Object>> result = dplStatement.execute(
				"select max(toChar(price.date,'yyyyMMdd HH:mm:ss')) as date,price.date as t from price group by price.date", getContext());
		assertEquals(5, result.size());
	}

	private ProcessorContext getContext() throws Exception {
		List<BeanPrice> price = new ArrayList<BeanPrice>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date[] dates = new Date[] { sdf.parse("20090101"), sdf.parse("20090102"), sdf.parse("20090103"), sdf.parse("20090104"), sdf.parse("20090105") };

		Date[] dates2 = new Date[] { sdf.parse("20100201"), sdf.parse("20100301"), sdf.parse("20100303"), sdf.parse("20010204"), sdf.parse("20110405") };
		Date[] dates3 = new Date[] { sdf.parse("20100101"), sdf.parse("20100501"), sdf.parse("20100603"), sdf.parse("20010204"), sdf.parse("20010405") };
		double[] value1 = new double[] { 2, 4, 6, 7, 5 };
		double[] value2 = new double[] { 3, 4, 3, 2, 1 };
		for (int i = 0; i < 5; i++) {
			BeanPrice p1 = new BeanPrice();
			BeanPrice p2 = new BeanPrice();
			p1.setId(i);
			p2.setId(i);
			p1.setCode("000001");
			p2.setCode("000002");
			p1.setDate(dates[i]);
			p2.setDate(dates[i]);
			p1.setDate2(dates2[i]);
			p2.setDate2(dates3[i]);
			p1.setPrice(value1[i]);
			p2.setPrice(value2[i]);
			price.add(p1);
			price.add(p2);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("price", price);
		return context;
	}
}

// $Id$