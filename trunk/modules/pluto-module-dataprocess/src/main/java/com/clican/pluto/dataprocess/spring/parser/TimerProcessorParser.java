/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.TimerProcessor;

public class TimerProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		String cronExpression = element.getAttribute("cronExpression");
		String startTime = element.getAttribute("startTime");
		String endTime = element.getAttribute("endTime");
		String taskScheduler = element.getAttribute("taskScheduler");
		String concurrent = element.getAttribute("concurrent");
		if (StringUtils.isEmpty(taskScheduler)) {
			taskScheduler = "taskScheduler";
		}
		String stepCommit = element.getAttribute("stepCommit");
		if(StringUtils.isNotEmpty(stepCommit)){
			beanDef.getPropertyValues().addPropertyValue("stepCommit", Boolean.parseBoolean(stepCommit));
		}
		beanDef.getPropertyValues().addPropertyValue("cronExpression", cronExpression);
		beanDef.getPropertyValues().addPropertyValue("startTime", startTime);
		beanDef.getPropertyValues().addPropertyValue("endTime", endTime);
		beanDef.getPropertyValues().addPropertyValue("taskScheduler", new RuntimeBeanReference(taskScheduler));
		if (StringUtils.isNotEmpty(concurrent)) {
			beanDef.getPropertyValues().addPropertyValue("concurrent", Boolean.parseBoolean(concurrent));
		}
		String[] timerProcessors = element.getAttribute("timerProcessors").split(",");
		List partitionProcessorList = new ManagedList();
		for (String timerProcessor : timerProcessors) {
			partitionProcessorList.add(new RuntimeBeanReference(timerProcessor.trim()));
		}
		beanDef.getPropertyValues().addPropertyValue("timerProcessors", partitionProcessorList);

	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return TimerProcessor.class;
	}

}

// $Id$