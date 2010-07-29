/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clican.pluto.dataprocess.engine.ProcessorContext;

/**
 * 
 * 默认的系统自带的<code>ProcessorContext</code>的实现。
 * <p>
 * 该实现主要简单的用一个同步Map来保存数据。并且使用简单的浅Copy来实现Clone方法。
 * 
 * @author wei.zhang
 * 
 */
public class ProcessorContextImpl implements ProcessorContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3995568536094249390L;

	private Map<String, Object> context = new HashMap<String, Object>();

	private String processorGroupName;

	public ProcessorContextImpl() {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			context.put("ip", ip);
		} catch (Exception e) {

		}

	}

	@SuppressWarnings("unchecked")
	
	public <T> T getAttribute(String name) {
		return (T) context.get(name);
	}

	
	public String[] getAttributeNames() {
		List<String> arrtibuteNames = new ArrayList<String>();
		synchronized (context) {
			for (String attributeName : context.keySet()) {
				arrtibuteNames.add(attributeName);
			}
		}
		return arrtibuteNames.toArray(new String[] {});
	}

	
	public void setAttribute(String name, Object value) {
		context.put(name, value);
	}

	@SuppressWarnings("unchecked")
	
	public ProcessorContext getCloneContext() {
		ProcessorContext processorContext = new ProcessorContextImpl();
		processorContext.setProcessorGroupName(this.processorGroupName);
		synchronized (context) {
			for (String attributeName : context.keySet()) {
				if (context.get(attributeName) instanceof List) {
					List<?> list = (List<?>) context.get(attributeName);
					processorContext.setAttribute(attributeName, new ArrayList(list));
				} else {
					processorContext.setAttribute(attributeName, context.get(attributeName));
				}

			}
		}
		return processorContext;
	}

	
	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		synchronized (context) {
			map.putAll(context);
		}
		return map;
	}

	
	public boolean contains(String name) {
		return context.containsKey(name);
	}

	
	public String getProcessorGroupName() {
		return processorGroupName;
	}

	
	public void setProcessorGroupName(String processorGroupName) {
		this.processorGroupName = processorGroupName;
	}

}

// $Id: ProcessorContextImpl.java 14912 2010-06-17 02:56:07Z wei.zhang $