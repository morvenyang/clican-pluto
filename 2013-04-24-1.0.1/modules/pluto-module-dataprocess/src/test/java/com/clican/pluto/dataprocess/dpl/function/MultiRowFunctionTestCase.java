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

import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class MultiRowFunctionTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testBeta() throws Exception {
		String dpl = "select beta(priceList1.price,priceList2.price) as beta from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testConvariance() throws Exception {
		String dpl = "select convariance(priceList1.price,priceList2.price) as convariance from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testVariance() throws Exception {
		String dpl = "select variance(priceList1.price) as variance from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testTreynorRatio() throws Exception {
		String dpl = "select treynorRatio(priceList1.price,priceList2.price) as treynorRatio from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testTrackError() throws Exception {
		String dpl = "select trackError(priceList1.price,priceList2.price) as trackError from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testStandardDeviation() throws Exception {
		String dpl = "select standardDeviation(priceList1.price) as standardDeviation from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testSharpeRatio() throws Exception {
		String dpl = "select sharpeRatio(priceList1.price) as sharpeRatio from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testSemiStandardDeviation() throws Exception {
		String dpl = "select SemiStandardDeviation(priceList1.price,priceList2.price) as semiStandardDeviation from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testRSquare() throws Exception {
		String dpl = "select rSquare(priceList1.price,priceList2.price) as rSquare from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testInformationRatio() throws Exception {
		String dpl = "select informationRatio(priceList1.price,priceList2.price) as rSquare from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testJensenAlpha () throws Exception {
		String dpl = "select jensenAlpha(priceList1.price,priceList2.price) as rSquare from priceList1,priceList2 where priceList1.id=priceList2.id";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testAtr() throws Exception {
		String dpl = "select atr(priceList1.price) as atr from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testAvg1() throws Exception {
		String dpl = "select avg(priceList1.price) as avg from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testAvg2() throws Exception {
		String dpl = "select avg(priceList1.price,5,7) as avg from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testCount() throws Exception {
		String dpl = "select count(priceList1.id) as count from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	

	public void testEcr() throws Exception {
		String dpl = "select ecr(priceList1.price) as avg from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testEqualRow() throws Exception {
		String dpl = "select equalRow(equal(priceList1.id,0),priceList1) as list1 from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testGroupList() throws Exception {
		String dpl = "select groupList(priceList1.id) as list1 from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testHedgeIndexRatio() throws Exception {
		String dpl = "select hedgeIndexRatio(priceList1.price,append(priceList1.id,'')) as list1 from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testHedgeIndexSize() throws Exception {
		String dpl = "select hedgeIndexSize(priceList1.price,append(priceList1.id,'')) as list1 from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testYieldByWeight() throws Exception {
		String dpl = "select yieldByWeight(priceList1.price,1,'priceList1') as list1 from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testIndexByWeight() throws Exception {
		String dpl = "select indexByWeight(priceList1.price,1,'priceList1',1000) as list1 from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}

	public void testMaxDrawdown() throws Exception {
		String dpl = "select maxDrawdown(priceList1.price,priceList1.date) as maxDrawdown from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testMaxRow() throws Exception {
		String dpl = "select maxRow(priceList1.price,priceList) as t from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testMinRow() throws Exception {
		String dpl = "select minRow(priceList1.price,priceList) as t from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testMin() throws Exception {
		String dpl = "select min(priceList1.price) as t from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testTotalReturnRatio() throws Exception {
		String dpl = "select totalReturnRatio(priceList1.price) as t from priceList1";
		List<Map<String, Object>> result = dplStatement.execute(dpl, getContext());
		assertEquals(1, result.size());
	}
	
	public void testMatchPriceAndWeight() throws Exception {
		String dpl = "select matchPriceAndWeight(priceList.date,priceList.code,priceList.price,priceList.code,0.5,'1D',dual.codeList) as index from priceList";
		ProcessorContext ctx = getContext();
		List<String> codes = new ArrayList<String>();
		codes.add("01");
		codes.add("02");
		ctx.setAttribute("codeList", codes);
		List<BeanPrice>  priceList1=ctx.getAttribute("priceList1");
		List<BeanPrice>  priceList2=ctx.getAttribute("priceList2");
		List<BeanPrice> priceList = new ArrayList<BeanPrice>();
		priceList.addAll(priceList1);
		priceList.addAll(priceList2);
		ctx.setAttribute("priceList", priceList);
		List<Map<String, Object>> result = dplStatement.execute(dpl, ctx);
		assertEquals(1, result.size());
	}
	
	private ProcessorContext getContext() throws Exception {
		// Fund
		List<BeanPrice> priceList1 = new ArrayList<BeanPrice>();
		List<BeanPrice> priceList2 = new ArrayList<BeanPrice>();
		Date current = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		double[] value1 = new double[] { 2, 4, 6, 7, 5, 3, 4, 3, 2, 1 };
		double[] value2 = new double[] { 2, 3, 4, 6, 2, 1, 5, 2, 1, 2 };
		for (int i = 0; i < 10; i++) {
			BeanPrice price1 = new BeanPrice();
			price1.setId(i);
			price1.setCode("01");
			price1.setDate(DateUtils.add(current, Calendar.DAY_OF_MONTH, i - 10));
			BeanPrice price2 = new BeanPrice();
			price2.setId(i);
			price2.setCode("02");
			price2.setDate(DateUtils.add(current, Calendar.DAY_OF_MONTH, i - 10));
			price1.setPrice(value1[i]);
			price2.setPrice(value2[i]);
			priceList1.add(price1);
			priceList2.add(price2);
		}

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("priceList1", priceList1);
		context.setAttribute("priceList2", priceList2);
		return context;
	}
}

// $Id$