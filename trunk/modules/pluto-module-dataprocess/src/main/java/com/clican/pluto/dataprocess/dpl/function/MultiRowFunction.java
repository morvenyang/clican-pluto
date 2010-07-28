/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 处理多行数据的函数接口
 * 
 * @author wei.zhang
 * 
 */
public interface MultiRowFunction extends Function {

	/**
	 * 递归调用函数,就是当有函数嵌套的时候需要递归的调要所有作为参数的函数。
	 * 
	 * @param rowSet
	 *            多行处理函数需要处理的数据组,如果没有group by则所有结果集合就是整个一个数据组
	 * @return 返回计算结果
	 * @throws CalculationException
	 *             如果有计算错误则抛出该错误
	 */
	public Object recurseCalculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException;

	/**
	 * 调用本身函数,就是调用多行函数真正的计算方法。
	 * 
	 * @param rowSet
	 *            多行处理函数需要处理的数据组,如果没有group by则所有结果集合就是整个一个数据组
	 * @return 返回计算结果
	 * @throws CalculationException
	 *             如果有计算错误则抛出该错误
	 */
	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException, PrefixAndSuffixException;

}

// $Id: MultiRowFunction.java 15865 2010-07-08 05:11:20Z wei.zhang $