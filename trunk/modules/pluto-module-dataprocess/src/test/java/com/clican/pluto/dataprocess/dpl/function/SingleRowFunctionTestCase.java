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

import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.testbean.BeanPrice;

public class SingleRowFunctionTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testAddDate() throws Exception {
		String dpl = "select addDate(priceList1.date,'1day') as day,addDate(priceList1.date,'1month') as month,addDate(priceList1.date,'-1year') as year from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testAppend() throws Exception {
		String dpl = "select append(priceList1.id,'1day') as day from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testBaseSingleRowFunction1() throws Exception {
		String dpl = "select append(priceList1.id,append(priceList1.id,'asd')) as t1 from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testBaseSingleRowFunction2() throws Exception {
		String dpl = "select append(avg(priceList1.price),'asd') as t1 from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testDayOfMonthAndQuarter() throws Exception {
		String dpl = "select dayOfMonth(priceList1.date) as day,dayOfQuarter(priceList1.date) as quarter from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testDecode() throws Exception {
		String dpl = "select decode(great(priceList1.id,5),'大于5','小于等于5') as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testDuration() throws Exception {
		String dpl = "select duration(priceList1.date,priceList1.date) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testEnumAvg() throws Exception {
		String dpl = "select enumAvg(priceList1.price,priceList1.id) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testHalfWeekCheck() throws Exception {
		String dpl = "select halfWeekCheck(priceList1.date,dual.dateList) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testYieldByEnumWeight() throws Exception {
		String dpl = "select yieldByEnumWeight(priceList1.price,0.5,'price1',priceList1.price,0.5,'price2') as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testIndexByEnumWeight() throws Exception {
		String dpl = "select indexByEnumWeight(priceList1.price,0.5,'price1',priceList1.price,0.5,'price2',1000) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	public void testIndexByRatio() throws Exception {
		String dpl = "select indexByRatio(priceList1.price,'true') as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testIsLeapYear() throws Exception {
		String dpl = "select isLeapYear(priceList1.date) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	 
	public void testIsNull() throws Exception {
		String dpl = "select isNull(priceList1.date) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testLeapYearDays() throws Exception {
		String dpl = "select leapYearDays(priceList1.date) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testLess() throws Exception {
		String dpl = "select less(priceList1.id,5) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testMatch() throws Exception {
		String dpl = "select match(priceList1.code,'1') as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testMaxDateMock() throws Exception {
		String dpl = "select maxDateMock(priceList1.date) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testMinDateMock() throws Exception {
		String dpl = "select minDateMock(priceList1.date) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testMaxOne() throws Exception {
		String dpl = "select maxOne(priceList1.price,100.0) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testMinOne() throws Exception {
		String dpl = "select minOne(priceList1.price,100.0) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testNumberFormat() throws Exception {
		String dpl = "select numberFormat(priceList1.price,'#.00') as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testPow() throws Exception {
		String dpl = "select pow(priceList1.price,2) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testResetWeight() throws Exception {
		String dpl = "select resetWeight(priceList1.price,toChar(priceList1.date)) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}

	public void testReturnRate() throws Exception {
		String dpl = "select returnRate(priceList1.price) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testRowNumber() throws Exception {
		String dpl = "select priceList1.id as t from priceList1 where rowNumber(dual.priceList1,priceList1)=1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testSevenDayPeriod() throws Exception {
		String dpl = "select sevenDayPeriod(toChar(priceList1.date)) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testSize() throws Exception {
		String dpl = "select size(dual.priceList1) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testSqrt() throws Exception {
		String dpl = "select sqrt(priceList1.price) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testSubString() throws Exception {
		String dpl = "select subString(priceList1.code,0,1) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testSwitch() throws Exception {
		String dpl = "select switch(priceList1.code,'0',0,'1',1,2) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testTotalVolume() throws Exception {
		String dpl = "select totalVolume(priceList1.price,100,1) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	public void testYearPeriod() throws Exception {
		String dpl = "select yearPeriod(priceList1.date) as t from priceList1";
		dplStatement.execute(dpl, getContext());
	}
	
	private ProcessorContext getContext() throws Exception {
		// Fund
		List<BeanPrice> priceList1 = new ArrayList<BeanPrice>();
		List<BeanPrice> priceList2 = new ArrayList<BeanPrice>();
		List<Date> dateList = new ArrayList<Date>();
		double[] value1 = new double[] { 2, 4, 6, 7, 5, 3, 4, 3, 2, 1 };
		double[] value2 = new double[] { 2, 3, 4, 6, 2, 1, 5, 2, 1, 2 };
		Date current = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		for (int i = 0; i < 10; i++) {
			BeanPrice price1 = new BeanPrice();
			price1.setId(i);
			price1.setCode(i+"");
			price1.setDate(DateUtils.add(current, Calendar.DAY_OF_MONTH, i - 10));
			BeanPrice price2 = new BeanPrice();
			price2.setId(i);
			price2.setDate(DateUtils.add(current, Calendar.DAY_OF_MONTH, i - 10));
			price1.setPrice(value1[i]);
			price2.setPrice(value2[i]);
			priceList1.add(price1);
			priceList2.add(price2);
			dateList.add(DateUtils.add(current, Calendar.DAY_OF_MONTH, i - 10));
		}

		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("priceList1", priceList1);
		context.setAttribute("priceList2", priceList2);
		context.setAttribute("dateList", dateList);
		return context;
	}
}

// $Id$