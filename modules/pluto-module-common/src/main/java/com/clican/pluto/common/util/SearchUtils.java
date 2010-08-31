/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.util;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

public class SearchUtils {

	/**
	 * 根据二分发查找出对应的数据，前提条件是source对象已经正序排序
	 * 
	 * @param <T>
	 *            被查找的对象类型
	 * @param <K>
	 *            被比较的对象
	 * @param source
	 *            被查找的对象集合
	 * @param comparablePropName
	 *            被查找的对象的用来比较的属性
	 * @param comp
	 *            被比较的对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> T binarySearch(List<T> source, String comparablePropName, Comparable<K> comp) {
		if (source == null || source.size() == 0) {
			return null;
		}
		int first = 0, upto = source.size();
		while (first < upto) {
			int midIdx = (first + upto) / 2;// Compute mid point.
			T mid = source.get(midIdx);
			try {
				K value = (K) PropertyUtils.getProperty(mid, comparablePropName);
				if (comp.compareTo(value) < 0) {
					upto = midIdx; // repeat search in bottom half.
				} else if (comp.compareTo(value) > 0) {
					first = midIdx + 1; // Repeat search in top half.
				} else {
					return source.get(midIdx);// Found it. return position
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null; // Failed to find key
	}

	/**
	 * 从正序排序的数据序列中查找离比较对象最接近的对象
	 * 
	 * @param <T>
	 *            被查找的对象类型
	 * @param <K>
	 *            被比较的对象
	 * @param source
	 *            被查找的对象集合
	 * @param comparablePropName
	 *            被查找的对象的用来比较的属性
	 * @param comp
	 *            被比较的对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> T searchPreviousNearestInput(List<T> source, String comparablePropName, Comparable<K> comp) {
		if (source == null || source.size() == 0) {
			return null;
		}
		for (int i = (source.size() - 1); i >= 0; i--) {
			T input = source.get(i);
			try {
				K value = (K) PropertyUtils.getProperty(input, comparablePropName);
				if (comp.compareTo(value) >= 0) {
					return input;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}
	

	/**
	 * This method implements the binary search algorithm.
	 * the list element and key should implement the comparable interface.
	 * @param list
	 *        must be sorted before, in ascendent order.
	 * @param key
	 *        the object that will be compared.
	 * @param before
	 *        if there is not a the same object, get the previous or next object
	 * @return
	 */
	public static <V extends Comparable<V>> V binarySearch(List<V> list, V key, boolean before) {
		if (list == null || list.size() == 0) {
			return null;
		}
		int low = 0;
		int high = list.size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Comparable<V> midVal = list.get(mid);
			midVal.compareTo(key);
			int cmp = midVal.compareTo(key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return list.get(mid); // key found
		}
		if (before) {
			int index = high;
			if (index < 0) {
				return null;
			}
			return list.get(index);
		} else {
			int index = low;
			if (index < 0) {
				return null;
			}
			if (index >= list.size()) {
				return null;
			}
			return list.get(index);
		}

	}


}

// $Id$