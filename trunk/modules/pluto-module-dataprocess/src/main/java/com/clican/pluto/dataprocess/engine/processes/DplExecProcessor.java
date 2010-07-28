/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.pluto.common.util.PropertyUtilS;
import com.clican.pluto.dataprocess.dpl.DplStatement;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 该类通过Data Process Language来处理数据
 * 
 * @author wei.zhang
 * 
 */
public class DplExecProcessor<T> extends BaseDataProcessor {

	private String dpl;

	private DplStatement dplStatement;

	private String resultName;

	private Class<T> clazz;

	private boolean singleRow;

	private List<String> traces;
	
	private final static Log tracesLog = LogFactory.getLog("com.jsw.dataprocess.engine.processes.DplExecProcessor.tracesLog");

	public void setDpl(String dpl) {
		this.dpl = dpl;
	}

	public void setDplStatement(DplStatement dplStatement) {
		this.dplStatement = dplStatement;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public boolean isSingleRow() {
		return singleRow;
	}

	public void setSingleRow(boolean singleRow) {
		this.singleRow = singleRow;
	}

	public List<String> getTraces() {
		return traces;
	}

	public void setTraces(List<String> traces) {
		this.traces = traces;
	}

	@Override
	public void process(ProcessorContext context) throws DataProcessException {
		log.trace("start processing dpl expression(" + this.getId() + ") ...");
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (this.clazz == null) {
				List<Map<String, Object>> result = dplStatement.execute(dpl, context);
				
				if (result == null) {
					if (log.isDebugEnabled()) {
						log.warn("dpl set attribute[" + resultName + "] to null(returned by statement)");
					}
					context.setAttribute(resultName, null);
					return;
				}
				
				if (log.isDebugEnabled()) {
					log.debug("dpl set attribute[" + resultName + "],size=" + result.size());
				}
				if (tracesLog.isDebugEnabled()) {
					if (traces != null && traces.size() != 0) {
						String debug = "\n";
						for (Map<String, Object> row : result) {
							for (String trace : traces) {
								Object obj = PropertyUtilS.getNestedProperty(row, trace);
								if (obj instanceof Date) {
									obj = sdf.format((Date) obj);
								}
								debug += trace = "[" + obj + "],";
							}
							debug += "\n";
						}
						tracesLog.debug(debug);
					}
				}
				if (singleRow) {
					if (result.size() > 0) {
						context.setAttribute(resultName, result.get(0));
					} else {
						context.setAttribute(resultName, null);
					}
				} else {
					context.setAttribute(resultName, result);
				}
			} else {
				List<T> result = dplStatement.execute(dpl, context, clazz);
				
				if (result == null) {
					if (log.isDebugEnabled()) {
						log.warn("set attribute[" + resultName + "] to null(returned by statement)");
					}
					context.setAttribute(resultName, null);
					return;
				}
				
				if (log.isDebugEnabled()) {
					log.debug("set attribute[" + resultName + "],size=" + result.size());
				}
				if (tracesLog.isDebugEnabled()) {
					if (traces != null && traces.size() != 0) {
						String debug = "\n";
						for (T row : result) {
							for (String trace : traces) {
								Object obj = null;
								if (row.getClass().isPrimitive() || Number.class.isAssignableFrom(row.getClass()) || row.getClass().equals(String.class)
										|| row.getClass().equals(Date.class)) {
									obj = row;
								} else {
									obj = PropertyUtilS.getNestedProperty(row, trace);
								}
								if (obj instanceof Date) {
									obj = sdf.format((Date) obj);
								}
								debug += trace = "[" + obj + "],";
							}
							debug += "\n";
						}
						tracesLog.debug(debug);
					}
				}
				if (singleRow) {
					if (result.size() > 0) {
						context.setAttribute(resultName, result.get(0));
					} else {
						context.setAttribute(resultName, null);
					}
				} else {
					context.setAttribute(resultName, result);
				}
			}
		} catch (Exception e) {
			throw new DataProcessException("dpl=[" + dpl + "]", e);
		} finally {
			log.trace("finish processing dpl expression(" + this.getId() + ")");
		}

	}
}

// $Id: DplExecProcessor.java 12937 2010-05-24 00:20:23Z wei.zhang $