/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.impl;


import com.clican.pluto.dataprocess.dpl.parser.DplParser;
import com.clican.pluto.dataprocess.dpl.parser.object.Pagination;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.DplParseException;

public class PagingParser implements DplParser {

	public final static String START_KEYWORD1 = "limit";

	public final static String START_KEYWORD2 = "offset";

	public final static String START_KEYWORD3 = "reverse";

	
	public Pagination parse(String dpl, ProcessorContext context) throws DplParseException {
		int index1 = dpl.indexOf(START_KEYWORD1);
		int index2 = dpl.indexOf(START_KEYWORD2);
		int index3 = dpl.indexOf(START_KEYWORD3);
		if (index1 < 0 && index2 < 0) {
			return null;
		}
		Pagination p = new Pagination();
		if(index1>0){
			p.setLimit(Integer.parseInt(dpl.substring(index1).split(" ")[1].trim()));
		}
		if(index2>0){
			p.setOffset(Integer.parseInt(dpl.substring(index2).split(" ")[1].trim()));
		}
		if (index3 > 0) {
			p.setReverse(true);
		}
		return p;
	}

}

// $Id: PagingParser.java 13276 2010-05-26 11:19:51Z wei.zhang $