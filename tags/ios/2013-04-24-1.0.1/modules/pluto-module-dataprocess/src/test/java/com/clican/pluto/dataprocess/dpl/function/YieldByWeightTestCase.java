/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
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
import com.clican.pluto.dataprocess.testbean.BeanPrice;
import com.clican.pluto.dataprocess.testbean.BeanWeight;

public class YieldByWeightTestCase extends BaseDplTestCase {
	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testCalculateYield() throws Exception {
		String dpl = "select yieldByWeight(price.price,weight.weight,price.code) as yield,summaryYieldByWeight(price.price,weight.weight,price.code) as summary from price,weight where price.code+=weight.code and price.date+=weight.date group by price.date";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(5, result.size());
	}

	private ProcessorContext getContext() throws Exception {
		// Fund
		List<BeanPrice> price = new ArrayList<BeanPrice>();
		List<BeanWeight> weight = new ArrayList<BeanWeight>();

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
			p1.setId(i);
			p2.setId(i);
			p1.setCode("000001");
			p2.setCode("000002");
			p1.setDate(dates.get(i));
			p2.setDate(dates.get(i));
			p1.setPrice(value1[i]);
			p2.setPrice(value2[i]);
			price.add(p1);
			price.add(p2);
		}
		BeanWeight w1 = new BeanWeight();
		w1.setCode("000001");
		w1.setDate(dates.get(0));
		w1.setWeight(0.4);
		w1.setId(0);
		BeanWeight w2 = new BeanWeight();
		w2.setCode("000002");
		w2.setDate(dates.get(0));
		w2.setWeight(0.6);
		w2.setId(0);
		BeanWeight w3 = new BeanWeight();
		w3.setCode("000001");
		w3.setDate(dates.get(3));
		w3.setWeight(0.3);
		w3.setId(3);
		BeanWeight w4 = new BeanWeight();
		w4.setCode("000002");
		w4.setDate(dates.get(3));
		w4.setWeight(0.7);
		w4.setId(3);
		weight.add(w1);
		weight.add(w2);
		weight.add(w3);
		weight.add(w4);
		
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("weight", weight);
		context.setAttribute("price", price);
		return context;
	}
}

// $Id$