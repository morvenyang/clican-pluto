/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.parser.object;

import java.util.List;

import com.clican.pluto.dataprocess.dpl.parser.ParserObject;
import com.clican.pluto.dataprocess.dpl.parser.bean.Group;

/**
 * 保存解析后的Group By的内容
 *
 * @author wei.zhang
 *
 */
public class GroupBy implements ParserObject {
	
	private List<Group> groups;

	public GroupBy(List<Group> groups){
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}
	
	
}


//$Id: GroupBy.java 12410 2010-05-13 06:55:57Z wei.zhang $