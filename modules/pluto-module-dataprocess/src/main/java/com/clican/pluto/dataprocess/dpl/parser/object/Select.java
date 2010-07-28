/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import java.util.List;

import com.clican.pluto.dataprocess.dpl.function.impl.BaseMultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.impl.BaseSingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.ParserObject;

/**
 * 保存解析后的Select的内容
 * 
 * @author wei.zhang
 * 
 */
public class Select implements ParserObject {

	private List<Object> columns;

	public Select(List<Object> columns) {
		this.columns = columns;
	}

	public List<Object> getColumns() {
		return columns;
	}

	public void setColumns(List<Object> columns) {
		this.columns = columns;
	}

	public boolean containMultiRowCalculation() {
		for (Object column : columns) {
			if (column instanceof BaseMultiRowFunction) {
				return true;
			} else if (column instanceof BaseSingleRowFunction) {
				if (((BaseSingleRowFunction) column).containMultiRowCalculation()) {
					return true;
				}
			}
		}
		return false;
	}
}

// $Id: Select.java 12468 2010-05-14 00:33:30Z wei.zhang $