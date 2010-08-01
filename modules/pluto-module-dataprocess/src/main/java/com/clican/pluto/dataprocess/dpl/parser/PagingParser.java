/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import com.clican.pluto.dataprocess.dpl.parser.object.Pagination;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public interface PagingParser {
	public Pagination parsePagination(String dpl, ProcessorContext context)
			throws DplParseException;

}

// $Id: PagingParser.java 13276 2010-05-26 11:19:51Z wei.zhang $