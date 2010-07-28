/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanA;
import com.clican.pluto.dataprocess.testbean.ViewBean;

public class DateFilterTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testCompareDate() throws Exception {
		String dpl = "select fund.* as fund,stock0.* as stock0,stock1.* as stock1,stock2.* as stock2,stock3.* as stock3,stock4.* as stock4,stock5.* as stock5,stock6.* as stock6,stock7.* as stock7,stock8.* as stock8,stock9.* as stock9 from fund, stock0,stock1,stock2,stock3,stock4,stock5,stock6,stock7,stock8,stock9 where fund.date=stock0.date and fund.date=stock1.date and fund.date=stock2.date and fund.date=stock3.date and fund.date=stock4.date and fund.date=stock5.date and fund.date=stock6.date and fund.date=stock7.date and fund.date=stock8.date and fund.date=stock9.date";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(96, result.size());
		List<ViewBean> list = dplStatement.execute(dpl, getContext(), ViewBean.class);
		assertEquals(96, list.size());
	}

	private ProcessorContext getContext() throws Exception {
		// Fund
		List<BeanA> fund = new ArrayList<BeanA>();
		// Stocks
		List<List<BeanA>> stocks = new ArrayList<List<BeanA>>();
		for (int i = 0; i < 10; i++) {
			stocks.add(new ArrayList<BeanA>());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> dates = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 100; i++) {
			dates.add(0, cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		Date date1 = sdf.parse("2009-01-01");
		Date date2 = sdf.parse("2009-01-02");
		Date date3 = sdf.parse("2009-01-03");
		Date date4 = sdf.parse("2009-01-04");
		Date date5 = sdf.parse("2009-01-05");
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 10; j++) {
				BeanA testA = new BeanA();
				testA.setName("stock" + j);
				testA.setDate(dates.get(i));
				if (i == 0 && j == 0) {
					testA.setDate(date2);
				}
				if (i == 1 && j == 1) {
					testA.setDate(date3);
				}
				if (i == 3 && j == 3) {
					testA.setDate(date4);
				}
				if (i == 4 && j == 4) {
					testA.setDate(date5);
				}
				stocks.get(j).add(testA);
			}
			BeanA testA2 = new BeanA();
			testA2.setName("fund");
			testA2.setDate(dates.get(i));
			fund.add(testA2);
		}

		BeanA testA2 = new BeanA();
		testA2.setName("fund");
		testA2.setDate(date1);
		fund.add(testA2);

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("fund", fund);
		for (int i = 0; i < stocks.size(); i++) {
			context.setAttribute("stock" + i, stocks.get(i));
		}
		return context;
	}
}

// $Id$