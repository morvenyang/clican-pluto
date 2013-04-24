/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author jerry.tian
 *
 */
package com.clican.pluto.dataprocess.testbean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.engine.ProcessorContext;

public class BeanExecutorBean {

	private Log logger = LogFactory.getLog(getClass());
	
	public String doSomething(ProcessorContext ctx) {
		logger.info("processor ctx: " + ctx);
		return "I am done!";
	}
}


//$Id$