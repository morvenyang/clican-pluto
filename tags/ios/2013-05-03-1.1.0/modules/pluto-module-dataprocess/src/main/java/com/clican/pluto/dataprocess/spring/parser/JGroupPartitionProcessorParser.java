/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.JGroupPartitionProcessor;

public class JGroupPartitionProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		((RootBeanDefinition) beanDef).setInitMethodName("init");
		String partition = element.getAttribute("partition");
		if (StringUtils.isEmpty(partition)) {
			partition = "partition";
		}
		String partitionListName = element.getAttribute("partitionListName");
		if (StringUtils.isNotEmpty(element.getAttribute("inputVarName"))) {
			String[] inputVarName = element.getAttribute("inputVarName").split(",");
			beanDef.getPropertyValues().addPropertyValue("inputVarName", inputVarName);
		}
		if (StringUtils.isNotEmpty(element.getAttribute("outputVarName"))) {
			String[] outputVarName = element.getAttribute("outputVarName").split(",");
			beanDef.getPropertyValues().addPropertyValue("outputVarName", outputVarName);
		}
		String serviceName = element.getAttribute("serviceName");
		beanDef.getPropertyValues().addPropertyValue("partitionListName", partitionListName);
		beanDef.getPropertyValues().addPropertyValue("serviceName", serviceName);
		try {
			beanDef.getPropertyValues().addPropertyValue("partition", new RuntimeBeanReference(partition));
		} catch (Throwable e) {

		}
		String[] partitionProcessors = element.getAttribute("partitionProcessors").split(",");
		List partitionProcessorList = new ManagedList();
		for (String partitionProcessor : partitionProcessors) {
			partitionProcessorList.add(new RuntimeBeanReference(partitionProcessor.trim()));
		}
		beanDef.getPropertyValues().addPropertyValue("partitionProcessors", partitionProcessorList);
	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return JGroupPartitionProcessor.class;
	}

}

// $Id$