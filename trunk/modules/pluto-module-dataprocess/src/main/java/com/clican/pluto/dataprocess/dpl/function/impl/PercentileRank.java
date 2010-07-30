/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
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
public class PercentileRank extends BaseSingleRowFunction {

	private PrefixAndSuffix numberOfTiles;
	private PrefixAndSuffix orderBy;
	private PrefixAndSuffix ascOrder;

	
	public Object calculate(Map<String, Object> row) throws CalculationException, PrefixAndSuffixException {
		throw new CalculationException("This method shall never be invoked");
	}

	class MapValueComparator implements Comparator<Map<String, Object>> {

		
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			try {
				Double c1 = (Double) orderBy.getValue(o1);
				Double c2 = (Double) orderBy.getValue(o2);
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

		double floor = ((Number) orderBy.getValue(rowSetCopy.get(0))).doubleValue();
		double ceiling = ((Number) orderBy.getValue(rowSetCopy.get(rowSetCopy.size() - 1))).doubleValue();

		int n = ((Number) numberOfTiles.getConstantsValue()).intValue();

		double step = (ceiling - floor) / n;
		if ("desc".equalsIgnoreCase((String) ascOrder.getConstantsValue())) {
			Collections.reverse(rowSetCopy);

			for (int rank = 1; rank <= n; rank++) {
				double rankBoundary = ceiling - step * rank;
				for (Map<String, Object> oneRow : rowSetCopy) {
					if (rowTileMap.containsKey(oneRow)) {
						continue;
					}

					double currValue = ((Number) orderBy.getValue(oneRow)).doubleValue();
					if (currValue > rankBoundary || (rank == n && currValue == floor)) {
						rowTileMap.put(oneRow, rank);
					} else {
						break;
					}
				}
			}
		} else {
			for (int rank = 1; rank <= n; rank++) {
				double rankBoundary = floor + step * rank;
				for (Map<String, Object> oneRow : rowSetCopy) {
					if (rowTileMap.containsKey(oneRow)) {
						continue;
					}

					double currValue = ((Number) orderBy.getValue(oneRow)).doubleValue();
					if (currValue < rankBoundary || (rank == n && currValue == ceiling)) {
						rowTileMap.put(oneRow, rank);
					} else {
						break;
					}
				}
			}
		}

		return rowTileMap.get(row);
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
}

// $Id$