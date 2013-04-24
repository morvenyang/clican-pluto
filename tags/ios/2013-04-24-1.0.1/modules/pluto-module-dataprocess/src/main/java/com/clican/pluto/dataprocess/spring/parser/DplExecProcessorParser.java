/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.DplExecProcessor;

/**
 * dpl元素的解析处理
 *
 * @author clican
 *
 */
public class DplExecProcessorParser extends AbstractProcessorParser {
	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		this.setBeanDefinitionStringProperty("resultName", beanDef, element);

		String dplStatement = element.getAttribute("dplStatement");
		if (StringUtils.isEmpty(dplStatement)) {
			dplStatement = "dplStatement";
		}
		String clazz = element.getAttribute("clazz");
		if (StringUtils.isNotEmpty(clazz)) {
			try {
				Class c = Class.forName(clazz);
				beanDef.getPropertyValues().addPropertyValue("clazz", c);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		String singleRow = element.getAttribute("singleRow");
		if (StringUtils.isNotEmpty(singleRow)) {
			beanDef.getPropertyValues().addPropertyValue("singleRow", Boolean.parseBoolean(singleRow));
		}
		String traces = element.getAttribute("traces");
		if (StringUtils.isNotEmpty(traces)) {
			List<String> traceList = new ArrayList<String>();
			for (String trace : traces.split(",")) {
				traceList.add(trace.trim());
			}
			beanDef.getPropertyValues().addPropertyValue("traces", traceList);
		}
		beanDef.getPropertyValues().addPropertyValue("dplStatement", new RuntimeBeanReference(dplStatement));
		beanDef.getPropertyValues().addPropertyValue("dpl", element.getTextContent().trim());
	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return DplExecProcessor.class;
	}

}

// $Id: DplExecProcessorParser.java 12414 2010-05-13 07:04:18Z wei.zhang $