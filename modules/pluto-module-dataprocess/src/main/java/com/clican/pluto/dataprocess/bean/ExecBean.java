/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.common.util.PropertyUtils;
import com.clican.pluto.dataprocess.engine.ProcessorContext;

public class ExecBean {

	/**
	 * 需要传递给IBatis的parameter在ProcessContext中的名称
	 */
	protected String paramName;
	/**
	 * 如果调用IBatis的Insert和Update是批量的，即用List或Set作为数据集则该值应该为<code>true</code>否则为
	 * <code>false</code>
	 * 
	 */
	protected boolean batch;

	/**
	 * 当从IBatis中查找得到一个对象的话，把该对象设置到<code>ProcessorContext</code>中使用的名字
	 */
	protected String resultName;

	/**
	 * 把Context中的变量根据名称映射来转换给ibatis的param map
	 */
	protected Map<String, String> paramNameMap;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public boolean isBatch() {
		return batch;
	}

	public void setBatch(boolean batch) {
		this.batch = batch;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public Map<String, String> getParamNameMap() {
		return paramNameMap;
	}

	public void setParamNameMap(Map<String, String> paramNameMap) {
		this.paramNameMap = paramNameMap;
	}

	@SuppressWarnings("unchecked")
	public Object getParam(ProcessorContext context) {
		try {
			Object param = context.getMap();
			if (StringUtils.isNotEmpty(getParamName())) {
				param = context.getAttribute(getParamName());
				if (param instanceof Map && getParamNameMap() != null && getParamNameMap().size() != 0) {
					param = new HashMap<String, Object>((Map) param);
					if (getParamNameMap() != null && getParamNameMap().size() != 0) {
						for (String contextName : getParamNameMap().keySet()) {
							Object obj = PropertyUtils.getNestedProperty(param, contextName);
							((Map) param).remove(contextName);
							if (obj != null) {
								((Map) param).put(getParamNameMap().get(contextName), obj);
							}
						}
					}
				}
			} else {
				Map<String, Object> map;
				if (getParamNameMap() != null && getParamNameMap().size() != 0) {
					map = context.getMap();
					for (String contextName : getParamNameMap().keySet()) {
						Object obj = PropertyUtils.getNestedProperty(map, contextName);
						map.remove(contextName);
						if (obj != null) {
							map.put(getParamNameMap().get(contextName), obj);
						}
					}
				} else {
					map = context.getMap();
				}
				param = map;
			}
			return param;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

// $Id: ExecBean.java 12410 2010-05-13 06:55:57Z wei.zhang $