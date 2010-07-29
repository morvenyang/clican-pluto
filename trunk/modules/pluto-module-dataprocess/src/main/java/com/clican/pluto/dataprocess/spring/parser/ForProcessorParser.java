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
import com.clican.pluto.dataprocess.engine.processes.ForProcessor;

/**
 * for元素的解析处理
 *
 * @author wei.zhang
 *
 */
public class ForProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		this.setBeanDefinitionStringProperty("elementName", beanDef, element);
		this.setBeanDefinitionStringProperty("step", beanDef, element);
		this.setBeanDefinitionStringProperty("start", beanDef, element);
		this.setBeanDefinitionStringProperty("end", beanDef, element);
		String commit = element.getAttribute("stepCommit");
		if(StringUtils.isNotEmpty(commit)){
			beanDef.getPropertyValues().addPropertyValue("stepCommit", Boolean.parseBoolean(commit));
		}
		String iteratorProcessors = element.getAttribute("iteratorProcessors");
		List iteratorProcessorList = new ManagedList();
		for (String nextDataProcess : iteratorProcessors.split(",")) {
			nextDataProcess = nextDataProcess.trim();
			iteratorProcessorList.add(new RuntimeBeanReference(nextDataProcess));
		}
		beanDef.getPropertyValues().addPropertyValue("iteratorProcessors", iteratorProcessorList);
	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return ForProcessor.class;
	}

}

// $Id: ForProcessorParser.java 13941 2010-06-03 09:08:40Z wei.zhang $