/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 
 * 算法说明
 * 
 * http://dev.jsw.office/svn/jsw_peweb/金思维评级方法论/评级过程中使用到的统计函数说明.odt
 * 
 * @author jerry.tian
 * 
 */
public class NTile extends BaseSingleRowFunction {

	private PrefixAndSuffix numberOfTiles;
	private PrefixAndSuffix orderBy;
	private PrefixAndSuffix ascOrder;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		throw new CalculationException("This method shall never be invoked");
	}

	class MapValueComparator implements Comparator<Map<String, Object>> {

		@SuppressWarnings("unchecked")
		
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			try {
				Comparable c1 = (Comparable) orderBy.getValue(o1);
				Comparable c2 = (Comparable) orderBy.getValue(o2);
				return c1.compareTo(c2);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

	
	public Object calculate(List<Map<String, Object>> rowSet, Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		Map<Object, Integer> rowTileMap = new HashMap<Object, Integer>();

		List<Map<String, Object>> rowSetCopy = new ArrayList<Map<String, Object>>(rowSet);

		MapValueComparator comparator = new MapValueComparator();

		Collections.sort(rowSetCopy, comparator);

		if ("desc".equalsIgnoreCase((String) ascOrder.getConstantsValue())) {
			Collections.reverse(rowSetCopy);
		}

		Integer n = ((Number) numberOfTiles.getConstantsValue()).intValue();

		int[] sizes = getTiledSizeArray(rowSet.size(), n);

		int[] indexes = getTiledIndexArray(sizes);

		for (int i = 0, c = 0, s = rowSetCopy.size(); i < s; i++) {
			if (i >= indexes[c]) {
				c++;
			}
			rowTileMap.put(rowSetCopy.get(i), (c + 1));
		}

		return rowTileMap.get(row);
	}

	private static int[] getTiledSizeArray(final int elementsSize, final int numberOfTiles) {
		if (numberOfTiles == 0) {
			throw new RuntimeException("tiles can not be zero!");
		}
		if (elementsSize == 0) {
			throw new RuntimeException("tiles can not used on zero elements result set!");
		}

		int[] sizes = new int[numberOfTiles];
		int size = elementsSize, nLeft = numberOfTiles;

		while (size > 0) {
			if (size % nLeft != 0) {
				sizes[numberOfTiles - nLeft] = size / nLeft + 1;
			} else {
				sizes[numberOfTiles - nLeft] = size / nLeft;
			}

			size -= (sizes[numberOfTiles - nLeft]);
			nLeft--;
		}

		return sizes;
	}

	private static int[] getTiledIndexArray(final int[] sizeArray) {
		int[] result = new int[sizeArray.length];

		System.arraycopy(sizeArray, 0, result, 0, sizeArray.length);

		for (int i = 1; i < sizeArray.length; i++) {
			result[i] += result[i - 1];
		}

		return result;
	}

	
	public boolean isSupportWhere() throws DplParseException {
		return false;
	}

	
	public void setParams(List<Object> params, ProcessorContext context) throws DplParseException {
		super.setParams(params, context);

		numberOfTiles = this.pasList.get(0);
		orderBy = this.pasList.get(1);
		ascOrder = this.pasList.get(2);
	}

	public static void main(String[] args) throws Exception {
		for (int i : getTiledSizeArray(26, 6)) {
			System.out.println(i);
		}

		System.out.println();

		for (int i : getTiledIndexArray(getTiledSizeArray(26, 6))) {
			System.out.println(i);
		}
	}
}

// $Id: NTile.java 15333 2010-06-24 15:56:24Z wei.zhang $