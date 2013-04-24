/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.MvelExecProcessor;

/**
 * mvel元素的解析处理
 *
 * @author clican
 *
 */
public class MvelExecProcessorParser extends AbstractProcessorParser {

	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		String mvelExpression =  element.getTextContent();
		String resultName =  element.getAttribute("resultName");
		beanDef.getPropertyValues().addPropertyValue("mvelExpression", mvelExpression);
		beanDef.getPropertyValues().addPropertyValue("resultName", resultName);
	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return MvelExecProcessor.class;
	}

}


//$Id: MvelExecProcessorParser.java 12414 2010-05-13 07:04:18Z wei.zhang $