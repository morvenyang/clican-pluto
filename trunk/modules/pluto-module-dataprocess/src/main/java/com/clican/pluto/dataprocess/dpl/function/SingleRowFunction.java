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
 * 单行处理函数接口定义，定义了单行处理函数的通用处理方式。
 * 
 * @author wei.zhang
 * 
 */
public interface SingleRowFunction extends Function {

	/**
	 * 递归调用函数,就是当有函数嵌套的时候需要递归的调要所有作为参数的函数。
	 * <p>
	 * 由于参数可能是多行函数也可能是单行函数所以rowSet和row都需要。
	 * 
	 * @param rowSet
	 *            单行row所在的组,如果没有group by就是整个集合是个组
	 * @param row
	 *            单行row
	 * @return 计算结果
	 * @throws CalculationException
	 *             如果有计算错误则抛出该错误
	 */
	public Object recurseCalculate(List<Map<String, Object>> rowSet, Map<String, Object> row) throws CalculationException, PrefixAndSuffixException;

	/**
	 * 调用本身函数,就是调用单行函数真正的计算方法。
	 * 
	 * @param row
	 *            单行row,该row已经包含嵌套函数的计算结果
	 * @return 计算结果
	 * @throws CalculationException
	 *             如果有计算错误则抛出该错误
	 */
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException;

	/**
	 * 该函数内是否包含多行处理函数
	 * 
	 * @return
	 */
	public boolean containMultiRowCalculation();
}

// $Id: SingleRowFunction.java 16002 2010-07-12 06:42:44Z wei.zhang $