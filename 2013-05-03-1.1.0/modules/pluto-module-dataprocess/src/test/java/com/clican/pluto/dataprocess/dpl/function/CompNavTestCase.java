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

public class CompNavTestCase extends BaseDplTestCase {
	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testCalculateCompNav() throws Exception {
		String dpl = "select compNav(nav.price,div.price,split.price,dual.previousNav,dual.previousCompNav) as compNav,fundDr(nav.price,div.price,split.price,dual.previousNav) as fundDr,nav.date as date from nav,div,split where nav.date+=div.date and nav.date+=split.date";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(10, result.size());
	}

	private ProcessorContext getContext() throws Exception {
		// Fund
		List<BeanPrice> navs = new ArrayList<BeanPrice>();
		List<BeanPrice> divs = new ArrayList<BeanPrice>();
		List<BeanPrice> splits = new ArrayList<BeanPrice>();
		
		List<Date> dates = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 10; i++) {
			dates.add(0, cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		double[] value = new double[] { 2, 4, 6, 7, 5,3, 4, 3, 2, 1  };
		for (int i = 0; i < 10; i++) {
			BeanPrice nav = new BeanPrice();
			BeanPrice div = new BeanPrice();
			BeanPrice split = new BeanPrice();
			nav.setDate(dates.get(i));
			nav.setPrice(value[i]);
			navs.add(nav);
			if(i==5||i==7){
				div.setDate(dates.get(i));
				div.setPrice(0.5);
			}
			if(i==5||i==8){
				split.setDate(dates.get(i));
				div.setPrice(0.9);
			}
		}

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("nav", navs);
		context.setAttribute("div", divs);
		context.setAttribute("split", splits);
		context.setAttribute("previousNav", 1.0d);
		context.setAttribute("previousCompNav", 1.1d);
		return context;
	}
}


//$Id$