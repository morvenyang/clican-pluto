/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.spring.parser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.clican.pluto.fsm.engine.IState;
import com.clican.pluto.fsm.engine.state.StartStateImpl;

public class StartStateParser extends AbstractStateParser {

	@Override
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
	}

	@Override
	public Class<? extends IState> getStateClass(Element element) {
		return StartStateImpl.class;
	}

}

// $Id$