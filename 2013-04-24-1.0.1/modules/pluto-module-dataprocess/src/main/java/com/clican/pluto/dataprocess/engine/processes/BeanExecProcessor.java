/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author jerry.tian
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import org.apache.commons.lang.StringUtils;

import com.clican.pluto.dataprocess.engine.JavaExecuteBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;
import com.clican.pluto.dataprocess.exception.InterruptedException;

/**
 * 用来执行 <code>JavaExecuteBean</code> 接口的处理器
 * 
 * @author jerry.tian
 * 
 */
public class BeanExecProcessor extends BaseDataProcessor {

	/**
	 * BeanExec处理后放回<code>ProcessorContext</code>的变量名
	 */
	private String resultName;

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	/**
	 * 被调用的Bean实例
	 */
	private JavaExecuteBean bean;

	public JavaExecuteBean getBean() {
		return bean;
	}

	public void setBean(JavaExecuteBean bean) {
		this.bean = bean;
	}

	
	public void process(ProcessorContext context) throws DataProcessException {
		try {
			this.realProcess(context);
		} catch (InterruptedException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new DataProcessException(ex);
		}
	}

	private void realProcess(ProcessorContext context) throws Exception {
		Object result = bean.processContext(context);
		if (result != null && StringUtils.isNotEmpty(resultName)) {
			context.setAttribute(resultName, result);
		}
	}

}

// $Id: BeanExecProcessor.java 15270 2010-06-24 04:08:10Z wei.zhang $