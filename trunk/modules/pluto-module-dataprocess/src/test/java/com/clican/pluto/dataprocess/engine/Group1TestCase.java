/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine;

import com.clican.pluto.dataprocess.BaseDataProcessTestCase;
import com.clican.pluto.dataprocess.engine.ProcessorContainer;

public class Group1TestCase extends BaseDataProcessTestCase {

	private ProcessorContainer processorContainer;

	public void setProcessorContainer(ProcessorContainer processorContainer) {
		this.processorContainer = processorContainer;
	}

	public void testProcessGroup1() throws Exception {
		processorContainer.processData("group1", null);
	}
}

// $Id$