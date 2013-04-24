/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import java.util.List;

import com.clican.pluto.dataprocess.dpl.function.impl.BaseMultiRowFunction;
import com.clican.pluto.dataprocess.dpl.function.impl.BaseSingleRowFunction;
import com.clican.pluto.dataprocess.dpl.parser.ParserObject;
import com.clican.pluto.dataprocess.dpl.parser.bean.Column;

/**
 * 保存解析后的Select的内容
 * 
 * @author clican
 * 
 */
public class Select implements ParserObject {

	private List<Column> columns;

	public Select(List<Column> columns) {
		this.columns = columns;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public boolean containMultiRowCalculation() {
		for (Column column : columns) {
			if (column.getPrefixAndSuffix().getFunction() instanceof BaseMultiRowFunction) {
				return true;
			} else if (column.getPrefixAndSuffix().getFunction() instanceof BaseSingleRowFunction) {
				if (((BaseSingleRowFunction) column.getPrefixAndSuffix().getFunction()).containMultiRowCalculation()) {
					return true;
				}
			}
		}
		return false;
	}
}

// $Id: Select.java 12468 2010-05-14 00:33:30Z wei.zhang $