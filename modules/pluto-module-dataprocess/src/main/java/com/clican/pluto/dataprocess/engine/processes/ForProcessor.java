/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.processes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.clican.pluto.common.util.PropertyUtilS;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.engine.impl.BaseDataProcessor;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * For循环遍历执行的Processor
 * 
 * @author clican
 * 
 */
public class ForProcessor extends BaseDataProcessor {

	private List<DataProcessor> iteratorProcessors;

	private String elementName;

	private String start;

	private String end;

	private String step;

	private boolean stepCommit;

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public List<DataProcessor> getIteratorProcessors() {
		return iteratorProcessors;
	}

	public void setIteratorProcessors(List<DataProcessor> iteratorProcessors) {
		this.iteratorProcessors = iteratorProcessors;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public boolean isStepCommit() {
		return stepCommit;
	}

	public void setStepCommit(boolean stepCommit) {
		this.stepCommit = stepCommit;
	}

	public void doWithoutCommit(DataProcessor processor, ProcessorContext context) throws DataProcessException {
		processor.beforeProcess(context);
		processor.process(context);
		processor.afterProcess(context);
	}

	
	public void process(ProcessorContext context) throws DataProcessException {
		try {
			Object startObj = null;
			Object endObj = null;
			String startChange = null;
			String endChange = null;
			if (StringUtils.isNotEmpty(start)) {
				if (start.contains("+")) {
					startObj = PropertyUtilS.getNestedProperty(context.getMap(), start.split("\\+")[0].trim());
					startChange = start.split("\\+")[1].trim();
				} else if (start.contains("-")) {
					startObj = PropertyUtilS.getNestedProperty(context.getMap(), start.split("\\-")[0].trim());
					startChange = "-" + start.split("\\-")[1].trim();
				} else {
					startObj = PropertyUtilS.getNestedProperty(context.getMap(), start);
					if (startObj == null) {
						startObj = start;
					}
				}

			}
			if (StringUtils.isNotEmpty(end)) {
				if (end.contains("+")) {
					endObj = PropertyUtilS.getNestedProperty(context.getMap(), end.split("\\+")[0].trim());
					endChange = end.split("\\+")[1].trim();
				} else if (end.contains("-")) {
					endObj = PropertyUtilS.getNestedProperty(context.getMap(), end.split("\\-")[0].trim());
					endChange = "-" + end.split("\\-")[1].trim();
				} else {
					endObj = PropertyUtilS.getNestedProperty(context.getMap(), end);
					if (endObj == null) {
						endObj = end;
					}
				}
			}

			if (startObj != null && endObj != null && NumberUtils.isNumber(startObj.toString()) && NumberUtils.isNumber(endObj.toString())
					&& NumberUtils.isNumber(step)) {
				int s = Integer.parseInt(startObj.toString());
				if (startChange != null) {
					s = s + Integer.parseInt(startChange);
				}
				int e = Integer.parseInt(endObj.toString());
				if (endChange != null) {
					e = e + Integer.parseInt(endChange);
				}
				int p = Integer.parseInt(step);
				for (int i = s; i < e; i = i + p) {
					if (log.isDebugEnabled()) {
						log.debug("ProcessorGroup[" + context.getProcessorGroupName() + "],开始循环处理[" + i + "/" + e + "]" + elementName + "=" + i);
					}
					context.setAttribute(elementName, i);
					for (DataProcessor iteratorProcessor : iteratorProcessors) {
						if (stepCommit) {
							dataProcessTransaction.doInCommit(iteratorProcessor, context);
						} else {
							doWithoutCommit(iteratorProcessor, context);
						}
					}
				}
			} else if (startObj instanceof Date && (endObj instanceof Date || endObj == null)) {
				if (startChange != null) {
					startObj = DateUtils.add((Date) startObj, this.getField(startChange), this.getChange(startChange));
				}
				int p = Integer.parseInt(step.replaceAll("day", "").replaceAll("month", "").replaceAll("year", ""));
				int field = -1;
				if (step.contains("day")) {
					field = Calendar.DAY_OF_MONTH;
				} else if (step.contains("month")) {
					field = Calendar.MONTH;
				} else if (step.contains("year")) {
					field = Calendar.YEAR;
				} else {
					throw new DataProcessException("解析for循环出错");
				}
				if (endObj == null) {
					endObj = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
				} else if (endChange != null) {
					endObj = DateUtils.add((Date) endObj, this.getField(endChange), this.getChange(endChange));
				}
				for (Date i = (Date) startObj; i.compareTo((Date) endObj) < 0; i = DateUtils.add(i, field, p)) {
					// 由于19900415和19910414是夏令制的问题,
					// 为了回避这个问题我们必须对时间在truncate一下,避免后续的时间带有多的那一个小时
					i = DateUtils.truncate(i, field);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					ProcessorContext subContext;
					if (this.isCloneContext()) {
						subContext = context.getCloneContext();
					} else {
						subContext = context;
					}
					if (log.isDebugEnabled()) {

						log.debug("ProcessorGroup[" + context.getProcessorGroupName() + "],开始循环处理[" + sdf.format(i) + "/" + sdf.format((Date) endObj) + "]"
								+ elementName + "=" + sdf.format(i));
					}
					subContext.setAttribute(elementName, i);
					for (DataProcessor iteratorProcessor : iteratorProcessors) {
						if (stepCommit) {
							dataProcessTransaction.doInCommit(iteratorProcessor, subContext);
						} else {
							doWithoutCommit(iteratorProcessor, subContext);
						}
					}
					if (this.propagations != null && this.propagations.size() > 0) {
						for (String propagation : propagations) {
							context.setAttribute(propagation, subContext.getAttribute(propagation));
						}
					}
				}
			} else {
				throw new DataProcessException("解析for循环出错");
			}
		} catch (DataProcessException e) {
			throw e;
		} catch (Exception e) {
			throw new DataProcessException(e);
		}

	}

	private int getChange(String expr) {
		int p = Integer.parseInt(expr.replaceAll("day", "").replaceAll("month", "").replaceAll("year", ""));
		return p;
	}

	private int getField(String expr) throws DataProcessException {
		int field = -1;
		if (expr.contains("day")) {
			field = Calendar.DAY_OF_MONTH;
		} else if (expr.contains("month")) {
			field = Calendar.MONTH;
		} else if (expr.contains("year")) {
			field = Calendar.YEAR;
		} else {
			throw new DataProcessException("解析for循环出错");
		}
		return field;
	}

}

// $Id: ForProcessor.java 16324 2010-07-20 10:20:12Z wei.zhang $