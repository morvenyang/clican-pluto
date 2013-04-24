/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.common.util;

/**
 * Provide a set of collection utilization method
 * <p>
 * 提供一组集合的工具方法
 * 
 * @author clican
 * 
 */
public class CollectionUtils {

	/**
	 * Using the counting sort to sort array.
	 * <p>
	 * 利用计数排序进行排序,该排序算法的时间复杂度是O(n)的
	 * <p>
	 * Please refer to
	 * <link>http://en.wikipedia.org/wiki/Sorting_algorithm#Counting_Sort</link>
	 * for detail algorithm.
	 * 
	 * @param array
	 *            the array to be sorted
	 * @param k
	 *            the max value in array
	 */
	public static int[] countingSort(int[] array, int k) {
		int[] count = new int[k + 1];
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			int pos = array[i];
			count[pos] = count[pos] + 1;
		}
		for (int i = 1; i <= k; i++) {
			count[i] = count[i] + count[i - 1];
		}
		for (int i = array.length - 1; i >= 0; i--) {
			result[count[array[i]]-1] = array[i];
			count[array[i]] = count[array[i]] - 1;
		}
		return result;
	}
}

// $Id$