/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.dpl.BaseDplTestCase;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.ProcessorContextImpl;

public class NTileTestCase extends BaseDplTestCase {

	private DplStatement dplStatement;

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void testNTile() throws Exception {
		double[] array = new double[] { 5.2, 6, 5.4, 5.8, 5.4, 5, 4, 5.4, 3.4, 3.2, 2.8, 3.4, 5.8, 6, 3.6, 4.6, 2.2, 2.2, 3, 5.6, 2, 3.6, 4.4, 3.2, 2, 4.2,
				4.6, 1.2, 3, 1.2, 5.6, 3.6, 3.8, 3.8, 3.6, 2.2, 5.2, 3.8, 4.4, 2.2, 3.6, 5.4, 3.2, 1.8, 1.4, 6, 4.8, 3.2, 4.6, 4.4, 3.2, 3.6, 2.4, 3.2, 2.6, 4,
				2.4, 3.2, 5.2, 3.8, 1, 3.4, 1, 1.2, 3.6, 6, 6, 1, 5.8, 4.8, 2.6, 3.4, 4.6, 2.8, 3.4, 5.4, 4.4, 6, 3.4, 4.4, 4.4, 2.4, 3.2, 4.2, 3.6, 1, 2.4, 3,
				4, 1.2, 1.6, 1.4, 1.6, 4.4, 2.8, 1, 1, 2, 2, 2.8, 1, 5.4, 3.4, 5.2, 4.4, 5.6, 5.4, 1.6, 2.6, 2.6, 1, 1.6, 2.8, 1.2, 3, 4, 4.4, 4.4, 5, 5.8, 5,
				1.8, 1, 3, 1.2, 1.8, 4, 1.8, 4.4, 6, 1, 2.4, 1.6, 5, 4, 3.6, 3.8, 3.8, 4, 1, 1, 5.4, 1, 1, 6, 2.2, 3.6, 1.2, 4.2, 5.2, 4.8, 5.8, 6, 5.8, 6, 5,
				4.8, 5, 5, 4, 5.6, 5, 2.4, 5.8, 1.2, 1, 1, 4.6, 2.4, 2.4, 3, 2, 2, 4.6, 5.2, 2.4, 2.8, 2, 2, 3.2, 3.8, 1, 5.2 };
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (double d : array) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value", d);
			list.add(map);
		}
		ProcessorContext context = new ProcessorContextImpl();
		context.setAttribute("list", list);
		List<Map<String,Object>> result = dplStatement.execute("select list.value as v,nTile(6,list.value,asc) as t from list order by v,t", context);
		assertEquals(183,result.size());
	}
}

// $Id$