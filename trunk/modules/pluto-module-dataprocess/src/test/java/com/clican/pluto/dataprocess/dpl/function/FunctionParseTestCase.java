/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.BaseDataProcessTestCase;
import com.clican.pluto.dataprocess.dpl.parser.impl.FromParser;
import com.clican.pluto.dataprocess.dpl.parser.impl.FunctionParser;
import com.clican.pluto.dataprocess.dpl.parser.object.From;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;

public class FunctionParseTestCase extends BaseDataProcessTestCase {

	private FunctionParser functionParser;

	public void setFunctionParser(FunctionParser functionParser) {
		this.functionParser = functionParser;
	}

	public void test1() throws Exception {
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("factor", 1);
		Map<String, Object> parseContext = new HashMap<String, Object>();
		List<String> vars = new ArrayList<String>();
		vars.add("weeklyReturnRatioList");
		vars.add("riskFreeList");
		vars.add("test");
		From from = new From(vars);
		parseContext.put(FromParser.START_KEYWORD, from);
		
		functionParser.parse(
				"sharpeRatio((weeklyReturnRatioList.returnRate-rFR(riskFreeList.rfrValue,50)))",
				context);
		functionParser.parse(
				"test.a+(test.b*test.c-(test.d*(test.e+test.f*test.r/test.c*equal(test.e,test.f))))+dual.factor/0.34+equal(test.a%test.b-test.p,test.c)",
				context);
	}
}

// $Id$