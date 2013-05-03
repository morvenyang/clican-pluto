/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author jerry.tian
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.lang.reflect.Method;

import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 使用类名运行时加载类并调用指定处理方法的processor实现。
 *
 * @author jerry.tian
 *
 */
public class ClassLoadExecProcessor extends BaseDataProcessor {

	private String clazzName;
	private String clazzInvokeMethod;
	
	public String getClazzInvokeMethod() {
		return clazzInvokeMethod;
	}

	public void setClazzInvokeMethod(String clazzInvokeMethod) {
		this.clazzInvokeMethod = clazzInvokeMethod;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	

	private String resultName;
	

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}


	private Class<?> clazz;
	private Method clazzMethod;
	private Object clazzInstance;
	
	
	public void process(ProcessorContext context) throws DataProcessException {
		try {
			this.realProcess(context);
		} catch (Exception ex) {
			throw new DataProcessException(ex);
		}
	}

	private void realProcess(ProcessorContext context) throws Exception {
		if (clazz == null) {
			clazz = Class.forName(clazzName);
			clazzMethod = clazz.getMethod(clazzInvokeMethod, ProcessorContext.class);
			clazzInstance = clazz.newInstance();
		}
		
		Object result = clazzMethod.invoke(clazzInstance, context);
		context.setAttribute(resultName, result);
	}
}


//$Id: ClassLoadExecProcessor.java 12410 2010-05-13 06:55:57Z wei.zhang $