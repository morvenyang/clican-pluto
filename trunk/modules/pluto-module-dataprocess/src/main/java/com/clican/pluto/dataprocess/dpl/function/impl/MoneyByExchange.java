/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 根据交易记录来计算当前的钱
 * 
 * 该函数会被按照日期排序的记录来多次聚合调用。因此结果集必须按照日期聚合排序。
 * <p>
 * 比如按照日期聚合后可能就会有多条不同股票的价格和交易记录。
 * <p>
 * 该函数就是利用这么多组数据来计算当前有多少钱。
 * 
 * yield(double price,double number,string code,double remainMoney)
 * 
 * price - 股票价格或基金净值，股票价格的话必须是复权后价格
 * <p>
 * weight - 权重数值，如果股票某日没有权重变化的则该数值为空
 * <p>
 * code - 股票或基金对应的代码，主要在内部计算的时候用来做标识
 * 
 * @author wei.zhang
 * 
 */
public class MoneyByExchange extends BaseMultiRowFunction {

	/**
	 * 函数参数
	 */
	protected PrefixAndSuffix pricePas;
	/**
	 * 函数参数
	 */
	protected PrefixAndSuffix numberPas;
	/**
	 * 函数参数
	 */
	protected PrefixAndSuffix keyPas;
	/**
	 * 函数参数
	 */
	protected PrefixAndSuffix remainMoneyPas;

	protected Double previousRemainMoney;

	protected Map<Object, Double> previousNumberMap = new HashMap<Object, Double>();

	@Override
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,PrefixAndSuffixException {
		double money = 0;
		for (Map<String, Object> row : rowSet) {
			Object key = keyPas.getValue(row);
			Double price = pricePas.getValue(row);
			Double number = numberPas.getValue(row);
			Double remainMoney = remainMoneyPas.getValue(row);
			if (number != null) {
				previousNumberMap.put(key, number);
			}
			if (remainMoney != null) {
				previousRemainMoney = remainMoney;
			}
			if (previousNumberMap.get(key) != null) {
				money += price * previousNumberMap.get(key);
			}
		}
		if (previousRemainMoney != null) {
			money += previousRemainMoney;
		}
		return money;
	}

	@Override
	public void setParams(List<Object> params, From from, ProcessorContext context) throws DplParseException {
		super.setParams(params, from, context);
		if (params == null || params.size() != 4) {
			throw new DplParseException();
		}
		this.pricePas = this.getPrefixeAndSuffix(params).get(0);
		this.numberPas = this.getPrefixeAndSuffix(params).get(1);
		this.keyPas = this.getPrefixeAndSuffix(params).get(2);
		this.remainMoneyPas = this.getPrefixeAndSuffix(params).get(3);
	}

}

// $Id: MoneyByExchange.java 12410 2010-05-13 06:55:57Z wei.zhang $