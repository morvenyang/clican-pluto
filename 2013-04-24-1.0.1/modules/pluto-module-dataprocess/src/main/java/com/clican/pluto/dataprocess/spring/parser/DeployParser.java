/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.dataprocess.bean.Deploy;

/**
 * deploy元素的解析处理
 *
 * @author jerry.tian
 *
 */
public class DeployParser implements BeanDefinitionParser {


	
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		BeanDefinitionRegistry bdr = parserContext.getRegistry();
		RootBeanDefinition beanDef = new RootBeanDefinition();
		beanDef.setAbstract(false);
		beanDef.setBeanClass(Deploy.class);
		beanDef.setLazyInit(false);
		beanDef.setAutowireMode(Autowire.BY_NAME.value());
		String id = element.getAttribute("id");
		if (StringUtils.isEmpty(id)) {
			id = "dplDeploy#" + element.hashCode();
		}
		bdr.registerBeanDefinition(id, beanDef);
		
		this.setBeanDefinitionStringProperty("name", beanDef, element);
		this.setBeanDefinitionStringProperty("url", beanDef, element);
		this.setBeanDefinitionStringProperty("propertyResources", beanDef, element);
		
		return beanDef;
	}
	
	public void setBeanDefinitionStringProperty(String propertyName, BeanDefinition beanDef, Element element) {
		String value =  element.getAttribute(propertyName);
		beanDef.getPropertyValues().addPropertyValue(propertyName, value);
	}
}


//$Id: DeployParser.java 12414 2010-05-13 07:04:18Z wei.zhang $