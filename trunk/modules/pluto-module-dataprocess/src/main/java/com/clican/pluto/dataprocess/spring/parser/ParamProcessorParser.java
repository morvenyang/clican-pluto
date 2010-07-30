/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.clican.pluto.dataprocess.bean.ParamBean;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.ParamProcessor;

/**
 * param元素的解析处理
 *
 * @author clican
 *
 */
public class ParamProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		List paramBeanList = new ManagedList();
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = node.getLocalName();
				if ("param".equals(localName)) {
					RootBeanDefinition bean = new RootBeanDefinition();
					bean.setAbstract(false);
					bean.setBeanClass(ParamBean.class);
					bean.setLazyInit(false);
					bean.setAutowireMode(Autowire.BY_NAME.value());
					Element paramElement = (Element) node;
					String paramName = paramElement.getAttribute("paramName");
					String paramValue = paramElement.getAttribute("paramValue");
					String type = paramElement.getAttribute("type");
					String override = paramElement.getAttribute("override");
					String pattern = paramElement.getAttribute("pattern");
					bean.getPropertyValues().addPropertyValue("paramName", paramName);
					bean.getPropertyValues().addPropertyValue("paramValue", paramValue);
					bean.getPropertyValues().addPropertyValue("type", type);
					bean.getPropertyValues().addPropertyValue("pattern", pattern);
					if (StringUtils.isNotEmpty(override)) {
						RootBeanDefinition over = new RootBeanDefinition();
						over.setAbstract(false);
						over.setBeanClass(Boolean.class);
						over.setLazyInit(false);
						over.getConstructorArgumentValues().addIndexedArgumentValue(0, override);
						bean.getPropertyValues().addPropertyValue("override", over);
					}
					paramBeanList.add(bean);
				}
			}
		}
		beanDef.getPropertyValues().addPropertyValue("paramBeanList", paramBeanList);
	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return ParamProcessor.class;
	}

}

// $Id: ParamProcessorParser.java 12414 2010-05-13 07:04:18Z wei.zhang $