/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;


import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 解析Data process language的接口
 * 
 * @author wei.zhang
 * 
 */
public interface DplParser {

	/**
	 * 通过解析dpl返回一个解析后的<code>ParserObject</code>
	 * 
	 * @param dpl
	 * @return
	 * @throws DplParseException
	 *             如果在解析过程中出现任何错误就抛出<code>DplParseException</code>
	 */
	public ParserObject parse(String dpl, ProcessorContext context) throws DplParseException;
}

// $Id: DplParser.java 12410 2010-05-13 06:55:57Z wei.zhang $