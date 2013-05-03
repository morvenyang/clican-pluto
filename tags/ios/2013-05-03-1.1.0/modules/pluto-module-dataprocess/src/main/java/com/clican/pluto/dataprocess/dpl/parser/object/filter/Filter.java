/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object.filter;

import com.clican.pluto.dataprocess.dpl.parser.ParserObject;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public abstract class Filter implements ParserObject {

	public abstract void filter(ProcessorContext context) throws DplParseException;

	/**
	 * Priority越大越先执行
	 * 
	 * @return
	 */
	public abstract int priority();

	/**
	 * 返回该Filter的表达式
	 * 
	 * @return
	 */
	public abstract String getExpr();

}

// $Id: Filter.java 13769 2010-06-01 11:51:43Z wei.zhang $