/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.eunmeration;

import com.clican.pluto.dataprocess.dpl.parser.eunmeration.FunctionOperation;

import junit.framework.TestCase;

public class FunctionOperationTestCase extends TestCase {

	public void testConvert() throws Exception {
		String expr = "+";
		assertEquals(FunctionOperation.PLUS, FunctionOperation.convert(expr));
	}

	public void testContainFirstPriorityOperation() throws Exception {
		assertTrue(FunctionOperation.containFirstPriorityOperation("a/b"));
		assertTrue(!FunctionOperation.containFirstPriorityOperation("a+b"));
	}

	public void testContainOperation() throws Exception {
		assertTrue(FunctionOperation.containOperation("a/b"));
		assertTrue(!FunctionOperation.containOperation("1.5"));
		assertTrue(!FunctionOperation.containOperation("a.*"));
	}
	
	public void testContainSecondPriorityOperation() throws Exception {
		assertTrue(!FunctionOperation.containSecondPriorityOperation("a/b"));
		assertTrue(FunctionOperation.containSecondPriorityOperation("a+b"));
	}
}

// $Id$