/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.spring.parser;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.engine.state.TaskStateImpl;

public class TaskStateParser extends AbstractStateParser {

	@Override
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		String taskName = element.getAttribute("taskName");
		beanDef.getPropertyValues().addPropertyValue("taskName", taskName);
		String taskType = element.getAttribute("taskType");
		beanDef.getPropertyValues().addPropertyValue("taskType", taskType);
		String assignees = element.getAttribute("assignees");
		beanDef.getPropertyValues().addPropertyValue("assignees", assignees);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends IState> getStateClass(Element element) {
		String clazz = element.getAttribute("clazz");
		if (StringUtils.isNotEmpty(clazz)) {
			try {
				return (Class<? extends IState>) Class.forName(clazz);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} else {
			return TaskStateImpl.class;
		}
	}

}

// $Id$