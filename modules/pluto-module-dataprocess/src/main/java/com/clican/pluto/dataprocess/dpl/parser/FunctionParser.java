/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import com.clican.pluto.dataprocess.dpl.parser.object.Function;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 用来解析select或where语句中function部分的解析器
 * 
 * @author clican
 * 
 */
public interface FunctionParser extends DplParser {

	public Function parse(String dpl, ProcessorContext context)
			throws DplParseException;

}

// $Id: FunctionParser.java 14982 2010-06-18 00:37:00Z wei.zhang $