/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.bean;

import com.clican.pluto.dataprocess.dpl.parser.eunmeration.OrderDir;

/**
 * 排序方式描述的临时对象
 *
 * @author clican
 *
 */
public class Order {
 
	/**
	 * 被排序的字段
	 * 比如list.name
	 */
	private String expr;
	
	/**
	 * 排序的方向
	 */
	private OrderDir orderDir;

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public OrderDir getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(OrderDir orderDir) {
		this.orderDir = orderDir;
	}
	
	
}


//$Id: Order.java 12410 2010-05-13 06:55:57Z wei.zhang $