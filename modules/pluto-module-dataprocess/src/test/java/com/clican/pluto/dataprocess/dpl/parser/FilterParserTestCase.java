/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.dpl.parser.impl.FilterParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.FromParser;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanA;
import com.clican.pluto.dataprocess.testbean.BeanB;
import com.clican.pluto.dataprocess.testbean.BeanC;

public class FilterParserTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	private FilterParser filterParser;

	private FromParser fromParser;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void setFilterParser(FilterParser filterParser) {
		this.filterParser = filterParser;
	}

	public void setFromParser(FromParser fromParser) {
		this.fromParser = fromParser;
	}

	public void testParseFilter() throws Exception {
		String dpl = "select list3.name as name,list1.date as date,sum(list2.id) as count from list1,list2,list3,list4 where list1.date=list4 and list1.id=list2.testaId and list2.id=list3.testbId group by list1.date,list3.name";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(240, result.size());

	}

	public void testParseFilter2() throws Exception {
		ProcessorContext context = new ProcessorContextImpl();
		String dpl = "select avg(expenseRatioList.expenseRatio) as avgExpenseRatio from expenseRatioList where (toChar(expenseRatioList.beginDate,'MMdd') = '0101' and toChar(expenseRatioList.endDate,'MMdd') = '0630') or (toChar(expenseRatioList.beginDate,'MMdd') = '0701' and toChar(expenseRatioList.endDate,'MMdd') = '1231')";
		fromParser.parse(dpl, context);
		filterParser.parse(dpl, context);
	}

	public void testParseFilter3() throws Exception {
		String dpl = "select list3.name as name,list1.date as date from list1,list2,list3,list4 where list1.date=list4 and list2.id=list3.testbId";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(24000, result.size());
	}

	public void testParseFilter4() throws Exception {
		String dpl = "select list3.name as name,list1.date as date from list1,list2,list3,list4 where list1.date=list4 and list2.id=list3.testbId and list2.id=0";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(80, result.size());
	}

	public void testParseFilter5() throws Exception {
		String dpl = "select list1.id as id from list1,list2,list3,list4,list5 where (list1.id=list2.id and list2.id=list3.id) and (list4.id=list5.id and list3.id=list4.id)";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(10, result.size());
	}

	public void testParseFilter6() throws Exception {
		String dpl = "select list1.id as id from list1,list2 where (list1.id=1 or list1.id=2) and list2.id=list1.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(2, result.size());
	}

	public void testParseFilter7() throws Exception {
		String dpl = "select list1.id as id from list1 where list1.id=1 or list1.id=2 or list1.id=3 or list1.id=4";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(4, result.size());
	}

	public void testParseFilter8() throws Exception {
		String dpl = "select list1.id as id from list1,list2,list3 where (list1.id=list2.id and list1.name=list2.name) and list3.id=1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(10, result.size());
	}

	public void testParseFilter9() throws Exception {
		String dpl = "select list1.id as id from list1,list2,list3 where (list1.id=list2.id and list1.name=list2.name) and 1=list3.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(10, result.size());
	}

	public void testParseFilter10() throws Exception {
		String dpl = "select list1.id as id from list1,list2 where (1=list1.id or 2=list1.id) and list2.id=list1.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(2, result.size());
	}

	public void testParseFilter11() throws Exception {
		String dpl = "select list1.id as id from list1 where 1=list1.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(1, result.size());
	}

	public void testParseFilter12() throws Exception {
		String dpl = "select list1.id as id from list1 where list1.id<10 and list1.id>=0 and list1.id<=10 and list1.id>0";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(9, result.size());
	}

	public void testParseFilter13() throws Exception {
		String dpl = "select list1.id as id from list1 where list1.id<10 and list1.id>=0 and list1.id<=10 and list1.id>0 and list1.name like '1' and list1.name not like '2'";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(1, result.size());
	}

	public void testParseFilter14() throws Exception {
		String dpl = "select list1.id as id from list1,list2 where list1.id>~list2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(10, result.size());
	}

	public void testParseFilter15() throws Exception {
		String dpl = "select list1.id as id from list1,list2 where list1.id<~list2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(10, result.size());
	}

	public void testParseFilter16() throws Exception {
		String dpl = "select list1.id as id from list1 where list1.id is not null";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(10, result.size());
	}

	public void testParseFilter17() throws Exception {
		String dpl = "select list1.id as id from list1 where list1.id is null";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(0, result.size());
	}

	public void testParseFilter18() throws Exception {
		String dpl = "select list1.id as id from list1 where list1.id is not empty";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(10, result.size());
	}

	public void testParseFilter19() throws Exception {
		String dpl = "select list1.id as id from list1 where list1.id is empty";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext2());
		assertEquals(0, result.size());
	}

	private ProcessorContext getContext2() throws Exception {
		List<BeanA> list1 = new ArrayList<BeanA>();
		for (int i = 0; i < 10; i++) {
			BeanA test = new BeanA();
			test.setId(i);
			test.setName("" + i);
			list1.add(test);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list1", list1);
		context.setAttribute("list2", new ArrayList<BeanA>(list1));
		context.setAttribute("list3", new ArrayList<BeanA>(list1));
		context.setAttribute("list4", new ArrayList<BeanA>(list1));
		context.setAttribute("list5", new ArrayList<BeanA>(list1));
		return context;
	}

	private ProcessorContext getContext() throws Exception {
		// NIV
		List<BeanA> list1 = new ArrayList<BeanA>();
		// HF
		List<BeanB> list2 = new ArrayList<BeanB>();
		// Person
		List<BeanC> list3 = new ArrayList<BeanC>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse("2009-01-01");
		Date date2 = sdf.parse("2009-01-02");
		Date date3 = sdf.parse("2009-01-03");
		Date date4 = sdf.parse("2009-01-04");
		Date date5 = sdf.parse("2009-01-05");
		List<Date> list4 = new ArrayList<Date>();
		list4.add(date1);
		list4.add(date2);
		list4.add(date3);
		list4.add(date4);
		for (int i = 0; i < 100; i++) {
			BeanA testA = new BeanA();
			testA.setId(i);
			if (i % 5 == 0) {
				testA.setDate(date1);
			} else if (i % 5 == 1) {
				testA.setDate(date2);
			} else if (i % 5 == 2) {
				testA.setDate(date3);
			} else if (i % 5 == 3) {
				testA.setDate(date4);
			} else if (i % 5 == 4) {
				testA.setDate(date5);
			}
			testA.setName("name" + i % 3);
			list1.add(testA);

			BeanB testB = new BeanB();
			testB.setId(i);
			testB.setName("name" + i);
			testB.setTestaId(i);
			list2.add(testB);
			testB = new BeanB();
			testB.setId(i + 1000);
			testB.setName("name" + i + 1000);
			testB.setTestaId(i);
			list2.add(testB);

			BeanC testC = new BeanC();
			testC.setId(i);
			testC.setName("name" + i);
			testC.setTestbId(i);
			list3.add(testC);
			testC = new BeanC();
			testC.setId(i + 1000);
			testC.setName("name" + i + 1000);
			testC.setTestbId(i + 1000);
			list3.add(testC);
			testC = new BeanC();
			testC.setId(i + 1001);
			testC.setName("name" + i + 1001);
			testC.setTestbId(i + 1000);
			list3.add(testC);
		}

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list1", list1);
		context.setAttribute("list2", list2);
		context.setAttribute("list3", list3);
		context.setAttribute("list4", list4);
		return context;
	}
}

// $Id$