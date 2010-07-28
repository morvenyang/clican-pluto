/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.ConditionProcessor;

/**
 * conditions元素的解析处理
 *
 * @author wei.zhang
 *
 */
public class ConditionProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	@Override
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		NodeList nodeList = element.getChildNodes();
		ManagedMap processorMap = new ManagedMap();
		ManagedMap exceptionMap = new ManagedMap();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = node.getLocalName();
				if ("cond".equals(localName)) {
					Element condElement = (Element) node;
					String expr = condElement.getAttribute("expr");
					String nextDataProcessor = condElement.getAttribute("nextDataProcessor");
					String exception = condElement.getAttribute("exception");
					if (StringUtils.isNotEmpty(exception)) {
						exceptionMap.put(expr, exception);
					} else if (StringUtils.isNotEmpty(nextDataProcessor)) {
						processorMap.put(expr, new RuntimeBeanReference(nextDataProcessor));
					} else {
						processorMap.put(expr, null);
					}
				}
			}
		}
		beanDef.getPropertyValues().addPropertyValue("exceptionMap", exceptionMap);
		beanDef.getPropertyValues().addPropertyValue("dataProcessorMap", processorMap);
	}

	@Override
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return ConditionProcessor.class;
	}

}

// $Id: ConditionProcessorParser.java 12414 2010-05-13 07:04:18Z wei.zhang $