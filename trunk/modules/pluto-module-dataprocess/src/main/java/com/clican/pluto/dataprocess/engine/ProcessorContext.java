/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据处理流程中的上下文环境，主要用于各个处理环节的数据交换。
 * 
 * @author wei.zhang
 * 
 */
public interface ProcessorContext extends Serializable {

	/**
	 * 根据变量名来获得对应的属性
	 * 
	 * @param <T>
	 * @param name
	 * @return
	 */
	public <T> T getAttribute(String name);

	/**
	 * 根据变量名和变量值来设置到<code>ProcessorContext</code>中，如果已经存在相同的变量名则覆盖其原有的变量值。
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value);

	/**
	 * 获取当前<code>ProcessorContext</code>中的所有的变量名。
	 * 
	 * @return
	 */
	public String[] getAttributeNames();

	/**
	 * 复制一份Clone的上下文
	 * 
	 * @return
	 */
	public ProcessorContext getCloneContext();

	/**
	 * 把Context中的attribute转换为Map
	 * 
	 * @return
	 */
	public Map<String, Object> getMap();

	/**
	 * 是否包含某个属性
	 * 
	 * @param name
	 * @return
	 */
	public boolean contains(String name);

	/**
	 * 返回当前的ProcessorGroup的名称
	 * 
	 * @return
	 */
	public String getProcessorGroupName();

	/**
	 * 设置当前的ProcessorGroup的名称
	 * 
	 * @param processorGroupName
	 */
	public void setProcessorGroupName(String processorGroupName);

}

// $Id: ProcessorContext.java 15290 2010-06-24 07:55:31Z wei.zhang $