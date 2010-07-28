/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

/**
 * 
 * 计算累积收益率
 * 
 * 该函数参数描述
 * 
 * 该函数会被按照日期排序的记录来多次聚合调用。因此结果集必须按照日期聚合排序。
 * <p>
 * 比如按照日期聚合后可能就会有多条不同股票的价格和权重记录。
 * <p>
 * 该函数就是利用这么各组数据和相对第一条数据来计算累积收益率。
 * 
 * summaryYield(double price,double weight,string code)
 * 
 * price - 股票价格或基金净值，股票价格的话必须是复权后价格
 * <p> 
 * weight - 权重数值，如果股票某日没有权重变化的则该数值为空 
 * <p> 
 * code - 股票或基金对应的代码，主要在内部计算的时候用来做标识
 * 
 * 
 * @author wei.zhang
 * 
 */
public class SummaryYieldByWeight extends YieldByWeight {

	@Override
	protected double getResult(double currentMoney) {
		return (currentMoney - DEFAULT_MONEY) / DEFAULT_MONEY;
	}
	
}

// $Id: SummaryYieldByWeight.java 12410 2010-05-13 06:55:57Z wei.zhang $