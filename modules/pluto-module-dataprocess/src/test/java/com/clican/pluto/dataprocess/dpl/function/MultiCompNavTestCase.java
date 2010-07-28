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
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class MultiCompNavTestCase extends BaseDplTestCase {
	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testCalculateMultiCompNav() throws Exception {
		String dpl = "select multiCompNav(nav.price,div.price,split.price) as compNav,nav.code as code from nav,div,split where nav.date+=div.date and nav.date+=split.date and nav.code=div.code and nav.code=split.code group by nav.code";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(2, result.size());
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
			BeanPrice nav1 = new BeanPrice();
			BeanPrice nav2 = new BeanPrice();
			nav1.setCode("000001");
			nav2.setCode("000002");
			BeanPrice div1 = new BeanPrice();
			BeanPrice div2 = new BeanPrice();
			div1.setCode("000001");
			div2.setCode("000002");
			BeanPrice split1 = new BeanPrice();
			BeanPrice split2 = new BeanPrice();
			split1.setCode("000001");
			split2.setCode("000002");
			nav1.setDate(dates.get(i));
			nav1.setPrice(value[i]);
			
			nav2.setDate(dates.get(i));
			nav2.setPrice(value[i]);
			
			
			navs.add(nav1);
			navs.add(nav2);
			if(i==5||i==7){
				div1.setDate(dates.get(i));
				div1.setPrice(0.5);
				
				div2.setDate(dates.get(i));
				div2.setPrice(0.5);
				
				divs.add(div1);
				divs.add(div2);
			}
			if(i==5||i==8){
				split1.setDate(dates.get(i));
				split1.setPrice(0.9);
				
				split2.setDate(dates.get(i));
				split2.setPrice(0.9);
				
				splits.add(split1);
				splits.add(split2);
			}
		}

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("nav", navs);
		context.setAttribute("div", divs);
		context.setAttribute("split", splits);
		return context;
	}
}


//$Id$