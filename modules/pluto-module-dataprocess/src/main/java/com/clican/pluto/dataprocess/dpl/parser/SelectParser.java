/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser;

import com.clican.pluto.dataprocess.dpl.parser.object.Select;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public interface SelectParser extends DplParser {

	public Select parse(String dpl, ProcessorContext context)
			throws DplParseException;
}

// $Id: SelectParser.java 13338 2010-05-27 08:22:07Z wei.zhang $