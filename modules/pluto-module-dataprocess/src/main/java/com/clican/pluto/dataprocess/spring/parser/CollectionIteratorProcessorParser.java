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
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.CollectionIteratorProcessor;

/**
 * 遍历Collection，针对其中的元素调用后续的processor。
 *
 * @author jerry.tian
 *
 */
public class CollectionIteratorProcessorParser extends AbstractProcessorParser {
	
	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		this.setBeanDefinitionStringProperty("elementName", beanDef, element);
		this.setBeanDefinitionStringProperty("collectionName", beanDef, element);
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
		return CollectionIteratorProcessor.class;
	}

}


//$Id: CollectionIteratorProcessorParser.java 14223 2010-06-06 05:51:26Z wei.zhang $