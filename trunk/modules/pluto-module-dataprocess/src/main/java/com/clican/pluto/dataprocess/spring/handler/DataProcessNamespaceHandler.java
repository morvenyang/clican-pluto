/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.spring.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.clican.pluto.dataprocess.spring.parser.BeanExecProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.CollectionIteratorProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.ConditionProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.DeployParser;
import com.clican.pluto.dataprocess.spring.parser.DplExecProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.ExcelProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.ForProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.IBatisExecProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.JGroupPartitionProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.JdbcExecProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.MapIteratorProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.MvelExecProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.ParamProcessorParser;
import com.clican.pluto.dataprocess.spring.parser.TimerProcessorParser;

/**
 * 自定义Spring Name Space XML处理器
 * 
 * @author wei.zhang
 * 
 */
public class DataProcessNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("ibatis", new IBatisExecProcessorParser());
		registerBeanDefinitionParser("mvel", new MvelExecProcessorParser());
		registerBeanDefinitionParser("bean", new BeanExecProcessorParser());
		registerBeanDefinitionParser("dpl", new DplExecProcessorParser());
		registerBeanDefinitionParser("collectionIterator", new CollectionIteratorProcessorParser());
		registerBeanDefinitionParser("mapIterator", new MapIteratorProcessorParser());
		registerBeanDefinitionParser("for", new ForProcessorParser());
		registerBeanDefinitionParser("condition", new ConditionProcessorParser());
		registerBeanDefinitionParser("jdbc", new JdbcExecProcessorParser());
		registerBeanDefinitionParser("params", new ParamProcessorParser());
		registerBeanDefinitionParser("excel", new ExcelProcessorParser());
		registerBeanDefinitionParser("deploy", new DeployParser());
		registerBeanDefinitionParser("partition", new JGroupPartitionProcessorParser());
		registerBeanDefinitionParser("timer", new TimerProcessorParser());
	}

}

// $Id: DataProcessNamespaceHandler.java 14938 2010-06-17 06:57:22Z wei.zhang $