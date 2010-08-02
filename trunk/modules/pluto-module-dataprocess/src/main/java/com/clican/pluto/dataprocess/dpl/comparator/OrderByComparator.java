/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.comparator;

import java.util.Comparator;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.parser.bean.Order;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.OrderDir;
import com.clican.pluto.dataprocess.dpl.parser.object.OrderBy;

/**
 * 用于Order By关键字的数据排序比较
 * 
 * @author clican
 * 
 */
public class OrderByComparator implements Comparator<Map<String, Object>> {

	private OrderBy orderBy;

	public OrderByComparator(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	@SuppressWarnings("unchecked")
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		for (Order order : orderBy.getOrders()) {
			String expr = order.getExpr();
			Object obj1 = o1.get(expr);
			Object obj2 = o2.get(expr);
			if (obj1 instanceof Comparable && obj2 instanceof Comparable) {
				int result = ((Comparable) obj1).compareTo((Comparable) obj2);
				if (result == 0) {
					continue;// using next order keyword
				}
				if (order.getOrderDir() == OrderDir.DESC) {
					return -result;
				} else {
					return result;
				}
			} else {
				continue;
			}
		}
		return 0;
	}

}

// $Id: OrderByComparator.java 15996 2010-07-12 06:36:09Z wei.zhang $