/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.comparator;

import java.util.Comparator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.common.util.PropertyUtilS;
import com.clican.pluto.dataprocess.dpl.parser.bean.Order;
import com.clican.pluto.dataprocess.dpl.parser.eunmeration.OrderDir;
import com.clican.pluto.dataprocess.dpl.parser.object.OrderBy;

/**
 * 用于Order By关键字的数据排序比较
 *
 * @author wei.zhang
 *
 */
public class OrderByComparator implements Comparator<Map<String, Object>> {

	private OrderBy orderBy;

	public OrderByComparator(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		for (Order order : orderBy.getOrders()) {
			String expr = order.getExpr();
			String prefix = null;
			String suffix = null;
			if (expr.indexOf(".") == -1) {
				suffix = expr;
			} else {
				prefix = expr.substring(0, expr.indexOf("."));
				suffix = expr.substring(expr.indexOf(".") + 1);
			}

			try {
				Object obj1 = null;
				Object obj2 = null;
				if (StringUtils.isEmpty(prefix)) {
					obj1 = o1.get(suffix);
					obj2 = o2.get(suffix);
					if (obj1 == null || obj2 == null) {
						continue;
					} else {
						if (obj1 instanceof Comparable && obj2 instanceof Comparable) {
							int ret = ((Comparable) obj1).compareTo((Comparable) obj2);

							if (ret == 0) {
								continue;// using next order keyword
							}

							if (order.getOrderDir() == OrderDir.DESC) {
								return -ret;
							} else {
								return ret;
							}
						} else {
							continue;
						}
					}
				} else {
					obj1 = o1.get(prefix);
					obj2 = o2.get(prefix);
					if (obj1 == null || obj2 == null) {
						continue;
					} else {
						obj1 = PropertyUtilS.getNestedProperty(obj1, suffix);
						obj2 = PropertyUtilS.getNestedProperty(obj2, suffix);
						if (obj1 instanceof Comparable && obj2 instanceof Comparable) {
							int ret = ((Comparable) obj1).compareTo((Comparable) obj2);

							if (ret == 0) {
								continue;// using next order
								// keyword
							}

							if (order.getOrderDir() == OrderDir.DESC) {
								return -ret;
							} else {
								return ret;
							}
						} else {
							continue;
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		return 0;
	}

}

// $Id: OrderByComparator.java 15996 2010-07-12 06:36:09Z wei.zhang $