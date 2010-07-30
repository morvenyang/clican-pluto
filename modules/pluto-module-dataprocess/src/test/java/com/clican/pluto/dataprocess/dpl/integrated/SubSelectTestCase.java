/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.integrated;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class SubSelectTestCase extends BaseDplTestCase {
	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testSubSelectInFrom() throws Exception {
		String dpl = "select t.date as date from (select list2.date from list2 where list2.id=0) as t";
		ProcessorContext pc = new ProcessorContextImpl();
		pc.setAttribute("list2", getList());
		List<Map<String, Object>> result = dplStatement.execute(dpl, pc);
		assertEquals(1, result.size());
	}
	
	public void testSubSelectInFromAndWhere1() throws Exception {
		String dpl = "select t.date as date from (select list2.date from list2 where list2.id=0) as t,list1 where list1.date=t.date and list1.id = (select list3.id from list3 where list3.id=0)";
		ProcessorContext pc = new ProcessorContextImpl();
		pc.setAttribute("list1", getList());
		pc.setAttribute("list2", getList());
		pc.setAttribute("list3", getList());
		List<Map<String, Object>> result = dplStatement.execute(dpl, pc);
		assertEquals(1, result.size());
	}
	
	public void testSubSelectInFromAndWhere2() throws Exception {
		String dpl = "select t.date as date from (select list2.date from list2 where list2.id!=0) as t,list1 where list1.date=t.date and list1.id in (select list3.id from list3 where list3.id!=0)";
		ProcessorContext pc = new ProcessorContextImpl();
		pc.setAttribute("list1", getList());
		pc.setAttribute("list2", getList());
		pc.setAttribute("list3", getList());
		List<Map<String, Object>> result = dplStatement.execute(dpl, pc);
		assertEquals(9, result.size());
	}

	private List<BeanPrice> getList() {
		List<Date> dates = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal = DateUtils.truncate(cal, Calendar.DAY_OF_MONTH);
		for (int i = 0; i < 10; i++) {
			dates.add(0, new Date(cal.getTime().getTime()));
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		List<BeanPrice> list = new ArrayList<BeanPrice>();
		for (int i = 0; i < 10; i++) {
			BeanPrice price = new BeanPrice();
			price.setId(i);
			price.setDate(dates.get(i));
			list.add(price);
		}
		return list;
	}
}

// $Id$