/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.BeanExecProcessor;

/**
 * bean元素的解析处理
 *
 * @author wei.zhang
 *
 */
public class BeanExecProcessorParser extends AbstractProcessorParser {
	
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		beanDef.getPropertyValues().addPropertyValue("bean", new RuntimeBeanReference( element.getAttribute("bean")));
		this.setBeanDefinitionStringProperty("resultName", beanDef, element);
	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return BeanExecProcessor.class;
	}

}


//$Id: BeanExecProcessorParser.java 15201 2010-06-23 07:43:46Z wei.zhang $