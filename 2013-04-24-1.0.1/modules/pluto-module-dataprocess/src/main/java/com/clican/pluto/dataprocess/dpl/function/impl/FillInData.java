/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.dpl.function.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.clican.pluto.dataprocess.dpl.parser.bean.PrefixAndSuffix;
import com.clican.pluto.dataprocess.exception.CalculationException;
import com.clican.pluto.dataprocess.exception.DplParseException;
import com.clican.pluto.dataprocess.exception.PrefixAndSuffixException;

/**
 * 根据标杆指数来填充数据
 * 
 * 该函数参数描述
 * 
 * fillInData(object obj,comparable comp,List<comparable> compList)
 * 
 * obj - 需要被填充的数据对象 comp - 需要被填充的数据对象的某个可以用来和标杆数据做比较的属性 compList - 标杆数据列表
 * 
 * 该函数返回数值是一个长度等于compList的List，List中的内容就是obj
 * 
 * @author clican
 * 
 */
public class FillInData extends BaseMultiRowFunction {

	public final static String FILL_IN_COMPARE_INDEX_NAME = "fillInCompareIndexName";

	private PrefixAndSuffix objectPas;

	private PrefixAndSuffix compPas;

	private List<Comparable<?>> compList;

	public Object calculate(List<Map<String, Object>> rowSet) throws CalculationException,
			PrefixAndSuffixException {
		
		List<Object> result = new ArrayList<Object>();
		int pos = -1;
		Object previousObject = null;
		Map<Comparable<?>, Map<String, Object>> map = new HashMap<Comparable<?>, Map<String, Object>>();
		for (Map<String, Object> row : rowSet) {
			Comparable<?> comp = compPas.getValue(row);
			map.put(comp, row);
		}
		try {
			for (int i = 0; i < compList.size(); i++) {
				Map<String, Object> row = map.get(compList.get(i));
				Object object = objectPas.getValue(row);
				if (object == null) {
					if (previousObject != null) {
						object = previousObject;
						pos = -1;
					} else {
						pos = i;
					}
					if (object != null) {
						Object temp = BeanUtils.cloneBean(object);
						BeanUtils.setProperty(temp, "date", compList.get(i));
					}
				} else {
					if (previousObject == null) {
						for (int j = 0; j <= pos; j++) {
							Object temp = BeanUtils.cloneBean(object);
							BeanUtils
									.setProperty(temp, "date", compList.get(j));
							result.set(j, temp);
						}
					}
					previousObject = object;
				}
				result.add(object);
			}
		} catch (Exception e) {
			throw new CalculationException(e);
		}
		return result;
	}

	public void setParams(List<Object> params) throws DplParseException {
		super.setParams(params);
		if (params == null || !(params.size() == 2 || params.size() == 3)) {
			throw new DplParseException();
		}
		this.objectPas = this.pasList.get(0);
		this.compPas = this.pasList.get(1);
		if (params.size() > 2) {
			try {
				this.compList = this.pasList.get(2).getConstantsValue();
			} catch (Exception e) {
				throw new PrefixAndSuffixException(e);
			}
		}
	}
}

// $Id: FillInData.java 13269 2010-05-26 10:15:18Z wei.zhang $