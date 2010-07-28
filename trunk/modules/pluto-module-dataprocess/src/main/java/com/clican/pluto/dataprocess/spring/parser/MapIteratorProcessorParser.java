/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.MapIteratorProcessor;

/**
 * mapIterator元素的解析处理
 *
 * @author wei.zhang
 *
 */
public class MapIteratorProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	@Override
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		this.setBeanDefinitionStringProperty("elementName", beanDef, element);
		this.setBeanDefinitionStringProperty("mapName", beanDef, element);
		String iteratorProcessors = element.getAttribute("iteratorProcessors");
		List iteratorProcessorList = new ManagedList();
		for (String nextDataProcess : iteratorProcessors.split(",")) {
			nextDataProcess = nextDataProcess.trim();
			iteratorProcessorList.add(new RuntimeBeanReference(nextDataProcess));
		}
		beanDef.getPropertyValues().addPropertyValue("iteratorProcessors", iteratorProcessorList);
	}

	@Override
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return MapIteratorProcessor.class;
	}

}


//$Id: MapIteratorProcessorParser.java 12414 2010-05-13 07:04:18Z wei.zhang $