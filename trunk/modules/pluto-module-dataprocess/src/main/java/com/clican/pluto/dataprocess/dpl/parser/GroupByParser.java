/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import com.clican.pluto.dataprocess.dpl.parser.object.GroupBy;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public interface GroupByParser extends DplParser {

	public GroupBy parse(String dpl, ProcessorContext context)
			throws DplParseException;

}

// $Id: GroupByParser.java 12410 2010-05-13 06:55:57Z wei.zhang $