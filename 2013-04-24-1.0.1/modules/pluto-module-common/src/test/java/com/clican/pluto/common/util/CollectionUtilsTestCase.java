/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.common.util;

import junit.framework.TestCase;

public class CollectionUtilsTestCase extends TestCase {

	public void testCountingTest() throws Exception {
		int array[] = new int[] { 2, 5, 3, 6, 9, 2, 5, 2, 1, 4, 6, 7, 3, 3 };
		int[] result = CollectionUtils.countingSort(array, 9);
		int[] expectedResult = new int[] { 1, 2, 2, 2, 3, 3, 3, 4, 5, 5, 6, 6, 7, 9 };
		for(int i=0;i<result.length;i++){
			assertEquals(expectedResult[i], result[i]);
		}
	}
}

// $Id$