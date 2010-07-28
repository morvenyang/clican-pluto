/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import java.util.ArrayList;
import java.util.List;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class ToDateTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}
	
	public void testToChar() throws Exception {
		List<String> result = dplStatement.execute("select toDate(price.code,yyyyMMdd) as date from price", getContext(),String.class);
		assertEquals(10,result.size());
	}
	
	private ProcessorContext getContext() throws Exception {
		List<BeanPrice> price = new ArrayList<BeanPrice>();
		for (int i = 0; i < 5; i++) {
			BeanPrice p1 = new BeanPrice();
			BeanPrice p2 = new BeanPrice();
			p1.setId(i);
			p2.setId(i);
			p1.setCode("20010101");
			p2.setCode("20010102");
			price.add(p1);
			price.add(p2);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("price", price);
		return context;
	}
}


//$Id$