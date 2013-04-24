/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.spring.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.clican.pluto.fsm.spring.parser.DecisionStateParser;
import com.clican.pluto.fsm.spring.parser.DefaultStateParser;
import com.clican.pluto.fsm.spring.parser.EndStateParser;
import com.clican.pluto.fsm.spring.parser.StartStateParser;
import com.clican.pluto.fsm.spring.parser.TaskStateParser;

/**
 * This handler is used to extend the spring xml to support fsm namespace.
 * 
 * @author wei.zhang
 * 
 */
public class FsmNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("start", new StartStateParser());
		registerBeanDefinitionParser("end", new EndStateParser());
		registerBeanDefinitionParser("state", new DefaultStateParser());
		registerBeanDefinitionParser("task", new TaskStateParser());
		registerBeanDefinitionParser("decision", new DecisionStateParser());
	}

}

// $Id$