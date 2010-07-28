/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 用来标识<code>com.jsw.dataprocess.engine.processes.BeanExecProcessor</code>
 * 的调用的bean的参数列表。
 * 
 * <p>
 * 所有的参数名就是对应参数在<code>com.jsw.dataprocess.engine.ProcessorContext</code>中的属性名
 * 
 * @author wei.zhang
 * 
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ExecBeanParam {

	/**
	 * 参数所对应的对象在<code>com.jsw.dataprocess.engine.ProcessorContext</code>中的属性名
	 * 
	 * @return
	 */
	String paramName();

}

// $Id: ExecBeanParam.java 12410 2010-05-13 06:55:57Z wei.zhang $