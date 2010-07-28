/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import java.util.List;

import com.clican.pluto.dataprocess.dpl.parser.ParserObject;
import com.clican.pluto.dataprocess.dpl.parser.bean.Order;

/**
 * 保存解析后的Order By的内容
 *
 * @author wei.zhang
 *
 */
public class OrderBy implements ParserObject {

	private List<Order> orders;

	public OrderBy(List<Order> orders) {
		this.orders = orders;
	}

	public List<Order> getOrders() {
		return orders;
	}

	

}

// $Id: OrderBy.java 12410 2010-05-13 06:55:57Z wei.zhang $