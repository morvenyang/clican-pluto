/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author jerry.tian
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.enumeration.TransactionMode;

/**
 * bean定义解析器，spring使用。
 * 
 * @author clican
 * 
 */
public abstract class AbstractProcessorParser implements BeanDefinitionParser {

	@SuppressWarnings({"unchecked","rawtypes"})
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		BeanDefinitionRegistry bdr = parserContext.getRegistry();
		RootBeanDefinition beanDef = new RootBeanDefinition();
		beanDef.setDestroyMethodName("destroy");
		beanDef.setAbstract(false);
		beanDef.setBeanClass(getDataProcessorClass());
		beanDef.setLazyInit(false);
		beanDef.setAutowireMode(Autowire.BY_NAME.value());
		String id = element.getAttribute("id");
		if (bdr.containsBeanDefinition(id)) {
			throw new RuntimeException("id[" + id + "]被重复定义了");
		}
		bdr.registerBeanDefinition(id, beanDef);
		beanDef.getPropertyValues().addPropertyValue("id", id);
		String startProcessor = element.getAttribute("startProcessor");
		boolean sp = false;
		if (StringUtils.isNotEmpty(startProcessor)) {
			sp = Boolean.parseBoolean(startProcessor);
		}
		beanDef.getPropertyValues().addPropertyValue("startProcessor", sp);

		String transaction = element.getAttribute("transaction");
		if (StringUtils.isNotEmpty(transaction)) {
			beanDef.getPropertyValues().addPropertyValue("transaction", transaction);
		} else if (sp) {
			beanDef.getPropertyValues().addPropertyValue("transaction", TransactionMode.BEGIN.getMode());
		}
		String cloneContext = element.getAttribute("cloneContext");
		if (StringUtils.isNotEmpty(cloneContext)) {
			beanDef.getPropertyValues().addPropertyValue("cloneContext", Boolean.parseBoolean(cloneContext));
		}
		String propagations = element.getAttribute("propagations");
		if (StringUtils.isNotEmpty(propagations)) {
			List<String> propataionList = new ArrayList<String>();
			for (String propagation : propagations.split(",")) {
				propataionList.add(propagation);
			}
			beanDef.getPropertyValues().addPropertyValue("propagations", propataionList);
		}
		String nextDataProcessors = element.getAttribute("nextDataProcessors");
		if (StringUtils.isNotEmpty(nextDataProcessors)) {
			List nextDataProcessList = new ManagedList();
			for (String nextDataProcess : nextDataProcessors.split(",")) {
				nextDataProcess = nextDataProcess.trim();
				nextDataProcessList.add(new RuntimeBeanReference(nextDataProcess));
			}
			beanDef.getPropertyValues().addPropertyValue("nextDataProcessors", nextDataProcessList);
		}

		customiseBeanDefinition(beanDef, element, parserContext);

		return beanDef;
	}

	public void setBeanDefinitionStringProperty(String propertyName, BeanDefinition beanDef, Element element) {
		String value = element.getAttribute(propertyName);
		beanDef.getPropertyValues().addPropertyValue(propertyName, value);
	}

	public abstract Class<? extends DataProcessor> getDataProcessorClass();

	public abstract void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext);
}

// $Id: AbstractProcessorParser.java 15178 2010-06-23 04:44:41Z wei.zhang $