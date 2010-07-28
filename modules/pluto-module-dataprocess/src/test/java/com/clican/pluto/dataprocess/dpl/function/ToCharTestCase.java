/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class ToCharTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testToChar() throws Exception {
		List<String> result = dplStatement.execute("select toChar(price.date) as date from price", getContext(), String.class);
		assertEquals(10, result.size());
	}

	public void testToCharInWhere() throws Exception {
		List<String> result = dplStatement.execute("select toChar(price.date) as date from price where toChar(price.date,yyyyMMdd)=20090104", getContext(),
				String.class);
		assertEquals(2, result.size());
	}

	private ProcessorContext getContext() throws Exception {
		List<BeanPrice> price = new ArrayList<BeanPrice>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date[] dates = new Date[] { sdf.parse("20090101"), sdf.parse("20090102"), sdf.parse("20090103"), sdf.parse("20090104"), sdf.parse("20090105") };
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