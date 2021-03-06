/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import java.util.List;

import com.clican.pluto.dataprocess.dpl.parser.ParserObject;
import com.clican.pluto.dataprocess.dpl.parser.impl.FromParserImpl;

/**
 * 保存解析后的From对象的内容
 * 
 * @author clican
 * 
 */
public class From implements ParserObject {

	private List<String> variableNames;

	public From(List<String> variableNames) {
		this.variableNames = variableNames;

	}

	public List<String> getVariableNames() {
		return variableNames;
	}

	/**
	 * 是否带有from中的变量前缀名
	 * 
	 * @param var
	 * @return
	 */
	public boolean containPrefix(String var) {
		for (String variable : variableNames) {
			if (var.startsWith(variable + ".") || var.equals(variable)) {
				return true;
			}
		}
		if (var.startsWith(FromParserImpl.CONSTANTS_KEY + ".")) {
			return true;
		}
		return false;
	}

}

// $Id: From.java 12410 2010-05-13 06:55:57Z wei.zhang $