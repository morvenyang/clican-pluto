/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 用于提供一个函数回调的接口
 * 
 * @author wei.zhang
 * 
 */
public interface FunctionCallback {

	/**
	 * 返回函数的计算结果，一般主要用于PrefixAndSuffix类中判断。 这个也是为了解决函数懒调用的问题。
	 * <p>
	 * 由于很多判断规则和函数的执行先后顺序希望有些函数在某些情况下不要被执行，因此函数真正的执行必须延后。
	 * 
	 * @return
	 * @throws CalculationException
	 * @throws PrefixAndSuffixException
	 */
	public Object getValue() throws CalculationException, PrefixAndSuffixException;
}

// $Id: FunctionCallback.java 15995 2010-07-12 06:35:36Z wei.zhang $