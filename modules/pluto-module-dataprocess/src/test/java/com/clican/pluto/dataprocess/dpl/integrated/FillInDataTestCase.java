/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.integrated;

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

/**
 * 该测试用例主要用来测试数据的填平操作。
 * 
 * 比如有3个股票从1/1～2/1的数据，但是每个股票拥有价格的天数是不一样的。
 * <p>
 * 然后用这3个股票和标杆指数做比较来填平数据。
 * 
 * @author wei.zhang
 * 
 */
public class FillInDataTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public static List<Date> hs300Date = new ArrayList<Date>();

	static {
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 15; i++) {
			hs300Date.add(0, cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	public void testFillInDataUsingPreviousData() throws Exception {
		String dpl = "select fillInData(price,price.date,dual.hs300Date) as prices,price.code as code from price group by price.code";
		ProcessorContext context = getContext();
		context.setAttribute("hs300Date", hs300Date);
		List<Map<String, Object>> result = dplStatement.execute(dpl, context);
		assertEquals(3, result.size());
	}

	private ProcessorContext getContext() throws Exception {
		List<BeanPrice> price = new ArrayList<BeanPrice>();
		double[] price1 = new double[] { 1.5, 1.2, 1.6, 1.7, 1.99, 1.5, 1.2, 1.6, 1.7, 1.99, 1.5, 1.2, 1.6, 1.7, 1.99 };
		double[] price2 = new double[] { 2.5, 2.2, 2.6, 2.7, 2.99, 2.5, 2.2, 2.6, 2.7, 2.99, 2.5, 2.2, 2.6, 2.7, 2.99 };
		double[] price3 = new double[] { 3.5, 3.2, 3.6, 3.7, 3.99, 3.5, 3.2, 3.6, 3.7, 3.99, 3.5, 3.2, 3.6, 3.7, 3.99 };

		for (int i = 0; i < 15; i++) {
			BeanPrice p1 = new BeanPrice();
			BeanPrice p2 = new BeanPrice();
			BeanPrice p3 = new BeanPrice();

			p1.setCode("000001");
			p2.setCode("000002");
			p3.setCode("000003");

			p1.setPrice(price1[i]);
			p2.setPrice(price2[i]);
			p3.setPrice(price3[i]);

			p1.setDate(hs300Date.get(i));
			p2.setDate(hs300Date.get(i));
			p3.setDate(hs300Date.get(i));

			if (i != 0 && i != 1 && i != 10) {
				price.add(p1);
			}
			if (i != 4 && i != 9) {
				price.add(p2);
			}
			if (i != 4 && i != 10 && i != 13 && i != 14) {
				price.add(p3);
			}
		}

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("price", price);
		return context;
	}
}

// $Id$