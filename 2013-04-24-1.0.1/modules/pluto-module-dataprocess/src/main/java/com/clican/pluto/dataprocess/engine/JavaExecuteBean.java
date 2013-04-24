/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author dapeng.zhang
 *
 */
package com.clican.pluto.dataprocess.engine;

/**
 * 此接口通过Java扩展Data　process的流程，此接口方法通过<code>BeanExecProcessor</code>
 * 来执行，任何实现此接口的类都可以作为DataProcess的某个节点的一部分
 * 
 * @author dapeng.zhang
 * 
 */
public interface JavaExecuteBean {

	/**
	 * @param context 的内容进行处理
	 */
	public Object processContext(ProcessorContext context) throws InterruptedException;
}

// $Id$