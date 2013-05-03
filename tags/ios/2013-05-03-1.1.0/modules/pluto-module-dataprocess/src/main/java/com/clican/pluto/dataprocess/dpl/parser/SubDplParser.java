/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import com.clican.pluto.dataprocess.dpl.parser.object.SubDpl;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

/**
 * 解析子查询语句
 * 
 * @author clican
 * 
 */
public interface SubDplParser {

	public SubDpl parseSubDpl(String dpl, ProcessorContext context)
			throws DplParseException;
}

// $Id$