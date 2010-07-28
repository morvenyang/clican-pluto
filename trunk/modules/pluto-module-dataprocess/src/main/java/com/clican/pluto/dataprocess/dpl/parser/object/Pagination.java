/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import com.clican.pluto.dataprocess.dpl.parser.ParserObject;

/**
 * 分页描述对象
 * 
 * @author wei.zhang
 * 
 */
public class Pagination implements ParserObject {

	/**
	 * 分页条数
	 */
	private Integer limit;

	/**
	 * 分页的起始位置
	 */
	private Integer offset = 0;

	/**
	 * 是否是反向分页，如果不是则和普通分页没有区别，如果是则从记录的尾部来计算分页的倒数的起始位置和分页条数
	 */
	private boolean reverse;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

}

// $Id: Pagination.java 16005 2010-07-12 06:45:03Z wei.zhang $