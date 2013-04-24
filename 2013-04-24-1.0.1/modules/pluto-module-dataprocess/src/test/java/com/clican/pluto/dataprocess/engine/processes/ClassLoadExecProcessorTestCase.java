/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import junit.framework.TestCase;

import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;
import com.clican.pluto.dataprocess.engine.processes.ClassLoadExecProcessor;
import com.clican.pluto.dataprocess.testbean.BeanA;

public class ClassLoadExecProcessorTestCase extends TestCase {

	public void test() throws Exception {
		ClassLoadExecProcessor p = new ClassLoadExecProcessor();
		p.setClazzName(BeanA.class.getName());
		p.setClazzInvokeMethod("process");
		p.setResultName("aaa");
		p.process(new ProcessorContextImpl());
	}
}

// $Id$