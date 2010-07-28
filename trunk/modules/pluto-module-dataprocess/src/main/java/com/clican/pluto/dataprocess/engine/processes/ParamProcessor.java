/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.bean.ParamBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.enumeration.ParamType;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 装填参数的Processor
 * 
 * @author wei.zhang
 * 
 */
public class ParamProcessor extends BaseDataProcessor {

	private List<ParamBean> paramBeanList;

	public void setParamBeanList(List<ParamBean> paramBeanList) {
		this.paramBeanList = paramBeanList;
	}

	@Override
	public void process(ProcessorContext context) throws DataProcessException {
		try {
			if (paramBeanList != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for (ParamBean pb : paramBeanList) {
					ParamType pt = ParamType.convert(pb.getType());
					Object value = null;
					if (pt == ParamType.STRING) {
						value = pb.getParamValue();
					} else if (pt == ParamType.CLAZZ) {
						value = Class.forName(pb.getParamValue());
					} else if (pt == ParamType.DATE) {
						if (StringUtils.isNotEmpty(pb.getPattern())) {
							value = new SimpleDateFormat(pb.getPattern()).parse(pb.getParamValue());
						} else {
							value = sdf.parse(pb.getParamValue());
						}
					} else if (pt == ParamType.DOUBLE) {
						value = Double.parseDouble(pb.getParamValue());
					} else if (pt == ParamType.LONG) {
						value = Long.parseLong(pb.getParamValue());
					} else if (pt == ParamType.INTEGER) {
						value = Integer.parseInt(pb.getParamValue());
					} else if (pt == ParamType.BOOLEAN) {
						value = Boolean.parseBoolean(pb.getParamValue());
					} else if (pt == ParamType.LIST) {
						List<String> list = new ArrayList<String>();
						for (String s : pb.getParamValue().split(",")) {
							list.add(s.trim());
						}
						value = list;
					} else {
						throw new DataProcessException("参数类型错误");
					}
					if (pb.isOverride()) {
						context.setAttribute(pb.getParamName(), value);
					} else {
						if (!context.contains(pb.getParamName())) {
							context.setAttribute(pb.getParamName(), value);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new DataProcessException("初始化参数错误", e);
		}

	}

}

// $Id: ParamProcessor.java 13776 2010-06-01 12:00:58Z wei.zhang $