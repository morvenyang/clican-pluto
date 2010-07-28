/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanExchange;
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class MoneyByExchangeTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testCalculateMoney() throws Exception {
		String dpl = "select moneyByExchange(price.price,exchange.number,price.code,exchange.remainMoney) as money from price,exchange where price.code+=exchange.code and toChar(price.date,yyyyMMdd)+=toChar(exchange.date,yyyyMMdd) group by price.date";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(5, result.size());
	}

	private ProcessorContext getContext() throws Exception {
		// Fund
		List<BeanPrice> price = new ArrayList<BeanPrice>();
		List<BeanExchange> exchange = new ArrayList<BeanExchange>();

		List<Date> dates = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 5; i++) {
			dates.add(0, cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		double[] value1 = new double[] { 2, 4, 6, 7, 5 };
		double[] value2 = new double[] { 3, 4, 3, 2, 1 };
		for (int i = 0; i < 5; i++) {
			BeanPrice p1 = new BeanPrice();
			BeanPrice p2 = new BeanPrice();
			p1.setCode("000001");
			p2.setCode("000002");
			p1.setDate(dates.get(i));
			p2.setDate(dates.get(i));
			p1.setPrice(value1[i]);
			p2.setPrice(value2[i]);
			price.add(p1);
			price.add(p2);
		}
		BeanExchange w1 = new BeanExchange();
		w1.setCode("000001");
		w1.setDate(dates.get(0));
		w1.setNumber(50d);
		w1.setRemainMoney(100d);
		BeanExchange w2 = new BeanExchange();
		w2.setCode("000002");
		w2.setDate(dates.get(0));
		w2.setNumber(60d);
		w2.setRemainMoney(126d);
		BeanExchange w3 = new BeanExchange();
		w3.setCode("000001");
		w3.setDate(dates.get(3));
		w3.setNumber(47d);
		w3.setRemainMoney(240d);
		BeanExchange w4 = new BeanExchange();
		w4.setCode("000002");
		w4.setDate(dates.get(3));
		w4.setNumber(68d);
		w4.setRemainMoney(221d);
		
		BeanExchange w5 = new BeanExchange();
		w5.setCode("000002");
		Date temp = dates.get(3);
		temp.setTime(temp.getTime()+1000);
		w5.setDate(temp);
		w5.setNumber(68d);
		w5.setRemainMoney(221d);
		
		exchange.add(w1);
		exchange.add(w2);
		exchange.add(w3);
		exchange.add(w4);
		exchange.add(w5);

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("exchange", exchange);
		context.setAttribute("price", price);
		return context;
	}
}

// $Id$