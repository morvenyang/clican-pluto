/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican, jing.tian
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.dataprocess.bean.IBatisExecBean;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * IBatis的statement的调用的Processor
 * 
 * @author clican
 * 
 */
public class IBatisExecProcessor extends BaseDataProcessor {

	private List<IBatisExecBean> ibatisExecBeanList;

	private Log logger = LogFactory.getLog(getClass());

	private SqlMapClient sqlMapClient;

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public void setIbatisExecBeanList(List<IBatisExecBean> ibatisExecBeanList) {
		this.ibatisExecBeanList = ibatisExecBeanList;
	}

	
	public void process(ProcessorContext context) throws DataProcessException {
		long nanoSeconds = System.nanoTime();
		logger.trace("start processing sql expression ...");
		if (ibatisExecBeanList != null) {
			for (IBatisExecBean ibatisExecBean : ibatisExecBeanList) {
				ibatisExecBean.execute(sqlMapClient, context);
			}
		}
		logger.trace("end processing sql expression, time consumed: " + (System.nanoTime() - nanoSeconds) / 1000000 + " ms");
	}

	private boolean convertToMap;

	public boolean isConvertToMap() {
		return convertToMap;
	}

	public void setConvertToMap(boolean convert2Map) {
		this.convertToMap = convert2Map;
	}
}

// $Id: IBatisExecProcessor.java 13776 2010-06-01 12:00:58Z wei.zhang $