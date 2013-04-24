/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.comparator;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.common.enumeration.Order;

public abstract class BaseComparator<T> implements Comparator<T> {

	protected final Log log = LogFactory.getLog(getClass());

	public BaseComparator() {

	}

	public BaseComparator(Order order) {
		this.order = order;
	}

	private Order order = Order.ASC;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	protected int convertByOrder(int result) {
		if (order == Order.ASC) {
			return result;
		} else if (order == Order.DESC) {
			return -result;
		} else {
			return 0;
		}
	}
}

// $Id$