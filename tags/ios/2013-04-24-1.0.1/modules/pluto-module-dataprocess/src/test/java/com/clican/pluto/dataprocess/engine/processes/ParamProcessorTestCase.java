/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.clican.pluto.dataprocess.bean.ParamBean;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.ParamProcessor;

public class ParamProcessorTestCase extends TestCase {

	public void test() throws Exception {
		ParamProcessor p = new ParamProcessor();
		List<ParamBean> list = new ArrayList<ParamBean>();
		ParamBean pb1 = new ParamBean();
		pb1.setParamName("pb1");
		pb1.setParamValue("1,2,3");
		pb1.setType("list");

		ParamBean pb2 = new ParamBean();
		pb2.setParamName("pb2");
		pb2.setParamValue("2010-01-01");
		pb2.setType("date");

		ParamBean pb3 = new ParamBean();
		pb3.setParamName("pb3");
		pb3.setParamValue("1");
		pb3.setType("int");

		ParamBean pb4 = new ParamBean();
		pb4.setParamName("pb4");
		pb4.setParamValue("1");
		pb4.setType("double");

		ParamBean pb5 = new ParamBean();
		pb5.setParamName("pb5");
		pb5.setParamValue("1");
		pb5.setType("long");

		ParamBean pb6 = new ParamBean();
		pb6.setParamName("pb6");
		pb6.setParamValue("true");
		pb6.setType("boolean");

		ParamBean pb7 = new ParamBean();
		pb7.setParamName("pb7");
		pb7.setParamValue("java.lang.String");
		pb7.setType("clazz");

		list.add(pb1);
		list.add(pb2);
		list.add(pb3);
		list.add(pb4);
		list.add(pb5);
		list.add(pb6);
		list.add(pb7);

		p.setParamBeanList(list);
		p.process(new ProcessorContextImpl());
	}
}

// $Id$