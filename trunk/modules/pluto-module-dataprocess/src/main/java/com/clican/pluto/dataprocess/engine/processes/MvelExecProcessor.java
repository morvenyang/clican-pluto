/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.MVEL;

import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * MVEL的执行块
 * 
 * @author wei.zhang
 * 
 */
public class MvelExecProcessor extends BaseDataProcessor {

	private String mvelExpression;

	private Log logger = LogFactory.getLog(getClass());

	private String resultName;

	public void setMvelExpression(String mvelExpression) {
		this.mvelExpression = mvelExpression;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public void setMvelExpressionInputStream(InputStream mvelExpressionInputStream) {
		try {
			byte[] data = new byte[mvelExpressionInputStream.available()];
			mvelExpressionInputStream.read(data);
			mvelExpression = new String(data, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (mvelExpressionInputStream != null) {
				try {
					mvelExpressionInputStream.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	@Override
	public void process(ProcessorContext context) throws DataProcessException {
		long nanoSeconds = System.nanoTime();
		logger.trace("start processing mvel expression(" + this.getId() + ") ...");
		if (StringUtils.isEmpty(mvelExpression)) {
			throw new DataProcessException("The MVEL Expression cannt be null");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (String attributeName : context.getAttributeNames()) {
			map.put(attributeName, context.getAttribute(attributeName));
		}
		map.put("context", context);
		Object obj = MVEL.eval(mvelExpression, map);
		if (StringUtils.isNotEmpty(resultName)) {
			context.setAttribute(resultName, obj);
		}
		logger.trace("end processing mvel expression(" + this.getId() + "), time consumed: " + (System.nanoTime() - nanoSeconds) / 1000000 + " ms");
	}

}

// $Id: MvelExecProcessor.java 13776 2010-06-01 12:00:58Z wei.zhang $