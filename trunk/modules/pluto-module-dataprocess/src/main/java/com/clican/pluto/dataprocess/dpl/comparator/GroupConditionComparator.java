/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.dpl.comparator;

import java.util.Comparator;
import java.util.List;

import com.clican.pluto.dataprocess.dpl.parser.bean.GroupCondition;

/**
 * 根据Group By的条件来做比较
 *
 * @author wei.zhang
 *
 */
public class GroupConditionComparator implements Comparator<List<GroupCondition>> {

	public int compare(List<GroupCondition> o1, List<GroupCondition> o2) {
		int size = o1.size();
		for (int i = 0; i < size; i++) {
			int result = o1.get(i).compareTo(o2.get(i));
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

}

// $Id: GroupConditionComparator.java 12410 2010-05-13 06:55:57Z wei.zhang $