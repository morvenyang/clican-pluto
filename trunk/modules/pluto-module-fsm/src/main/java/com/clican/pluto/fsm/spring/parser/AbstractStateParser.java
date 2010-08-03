/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author jerry.tian
 *
 */
package com.clican.pluto.fsm.spring.parser;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.clican.pluto.fsm.engine.IState;

/**
 * The abstract base state parser.
 * 
 * @author clican
 * 
 */
public abstract class AbstractStateParser implements BeanDefinitionParser {

	@SuppressWarnings("unchecked")
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		BeanDefinitionRegistry bdr = parserContext.getRegistry();
		RootBeanDefinition beanDef = new RootBeanDefinition();
		beanDef.setDestroyMethodName("destroy");
		beanDef.setAbstract(false);
		beanDef.setBeanClass(getStateClass(element));
		beanDef.setLazyInit(false);
		beanDef.setAutowireMode(Autowire.BY_NAME.value());
		String name = element.getAttribute("name");
		if (bdr.containsBeanDefinition(name)) {
			throw new RuntimeException("name[" + name + "]is defined duplicated");
		}
		bdr.registerBeanDefinition(name, beanDef);
		beanDef.getPropertyValues().addPropertyValue("name", name);
		String value = element.getAttribute("value");
		beanDef.getPropertyValues().addPropertyValue("value", value);

		String propagation = element.getAttribute("propagation");
		beanDef.getPropertyValues().addPropertyValue("propagation", propagation);
		String nextStates = element.getAttribute("nextStates");
		if (StringUtils.isNotEmpty(nextStates)) {
			List nextStateList = new ManagedList();
			for (String nextState : nextStates.split(",")) {
				nextStateList.add(new RuntimeBeanReference(nextState.trim()));
			}
			beanDef.getPropertyValues().addPropertyValue("nextStates", nextStateList);
		}

		String previousStates = element.getAttribute("previousStates");
		if (StringUtils.isNotEmpty(previousStates)) {
			List previousStateList = new ManagedList();
			for (String previousState : previousStates.split(",")) {
				previousStateList.add(new RuntimeBeanReference(previousState.trim()));
			}
			beanDef.getPropertyValues().addPropertyValue("previousStates", previousStateList);
		}

		NodeList nodeList = element.getChildNodes();
		Map nextCondStates = new ManagedMap();
		Map params = new ManagedMap();
		List startListeners = new ManagedList();
		List endListeners = new ManagedList();
		Map timeoutListeners = new ManagedMap();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = node.getLocalName();
				Element e = (Element) node;
				if ("nextCondStates".equals(localName)) {
					String expr = e.getAttribute("expr");
					nextStates = e.getAttribute("nextStates");
					List nextStateList = new ManagedList();
					if (StringUtils.isNotEmpty(nextStates)) {
						for (String nextState : nextStates.split(",")) {
							nextStateList.add(new RuntimeBeanReference(nextState.trim()));
						}
					}
					nextCondStates.put(expr, nextStateList);
				} else if ("param".equals(localName)) {
					String paramName = e.getAttribute("name");
					String paramValue = e.getAttribute("value");
					params.put(paramName, paramValue);
				} else if ("startListener".equals(localName) || "endListener".equals(localName) || "timeoutListener".equals(localName)) {
					String clazz = e.getAttribute("clazz");
					String listener = e.getAttribute("listener");
					Object obj;
					if (StringUtils.isNotEmpty(clazz)) {
						try {
							RootBeanDefinition bean = new RootBeanDefinition();
							bean.setAbstract(false);
							bean.setBeanClass(Class.forName(clazz));
							bean.setLazyInit(false);
							bean.setAutowireMode(Autowire.BY_NAME.value());
							if ("timeoutListner".equals(localName)) {
								String timeoutName = e.getAttribute("name");
								String startTime = e.getAttribute("startTime");
								String dueTime = e.getAttribute("dueTime");
								String repeatTime = e.getAttribute("repeatTime");
								String repeatDuration = e.getAttribute("repeatDuration");
								String businessCalendarName = e.getAttribute("businessCalendarName");
								bean.getPropertyValues().addPropertyValue("name", timeoutName);
								bean.getPropertyValues().addPropertyValue("startTime", startTime);
								bean.getPropertyValues().addPropertyValue("dueTime", dueTime);
								bean.getPropertyValues().addPropertyValue("repeatTime", repeatTime);
								bean.getPropertyValues().addPropertyValue("repeatDuration", repeatDuration);
								bean.getPropertyValues().addPropertyValue("businessCalendarName", businessCalendarName);
							}
							NodeList nodeList2 = element.getChildNodes();
							Map params2 = new ManagedMap();
							for (int j = 0; j < nodeList2.getLength(); j++) {
								Node node2 = nodeList2.item(j);
								if (node2.getNodeType() == Node.ELEMENT_NODE) {
									String localName2 = node2.getLocalName();
									if ("param".equals(localName2)) {
										String paramName = ((Element) node2).getAttribute("name");
										String paramValue = ((Element) node2).getAttribute("value");
										params2.put(paramName, paramValue);
									}
								}
							}
							obj = bean;
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					} else if (StringUtils.isNotEmpty(listener)) {
						obj = new RuntimeBeanReference(listener.trim());
					} else {
						throw new RuntimeException("There must be clazz or listener parameter setting");
					}
					if ("startListener".equals(localName)) {
						startListeners.add(obj);
					} else if ("endListener".equals(localName)) {
						endListeners.add(obj);
					} else {
						String timeoutName = e.getAttribute("name");
						timeoutListeners.put(timeoutName, obj);
					}
				}
			}
		}
		beanDef.getPropertyValues().addPropertyValue("nextCondStates", nextCondStates);
		beanDef.getPropertyValues().addPropertyValue("params", params);
		beanDef.getPropertyValues().addPropertyValue("startListeners", startListeners);
		beanDef.getPropertyValues().addPropertyValue("endListeners", endListeners);
		beanDef.getPropertyValues().addPropertyValue("timeoutListeners", timeoutListeners);
		
		customiseBeanDefinition(beanDef, element, parserContext);
		return beanDef;
	}

	public void setBeanDefinitionStringProperty(String propertyName, BeanDefinition beanDef, Element element) {
		String value = element.getAttribute(propertyName);
		beanDef.getPropertyValues().addPropertyValue(propertyName, value);
	}

	public abstract Class<? extends IState> getStateClass(Element element);

	public abstract void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext);
}

// $Id: AbstractStateParser.java 15178 2010-06-23 04:44:41Z wei.zhang $