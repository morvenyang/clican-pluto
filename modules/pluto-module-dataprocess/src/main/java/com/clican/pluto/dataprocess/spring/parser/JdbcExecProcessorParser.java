/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.clican.pluto.dataprocess.bean.JdbcExecBean;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.JdbcProcessor;

/**
 * jdbc元素的解析处理
 *
 * @author wei.zhang
 *
 */
public class JdbcExecProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		String jdbcTemplate = element.getAttribute("jdbcTemplate");
		beanDef.getPropertyValues().addPropertyValue("jdbcTemplate", new RuntimeBeanReference(jdbcTemplate));
		List jdbcExecBeanList = new ManagedList();
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = node.getLocalName();
				if ("exec".equals(localName)) {
					RootBeanDefinition bean = new RootBeanDefinition();
					bean.setAbstract(false);
					bean.setBeanClass(JdbcExecBean.class);
					bean.setLazyInit(false);
					bean.setAutowireMode(Autowire.BY_NAME.value());
					
					Element jdbcExecElement = (Element) node;
					String batch = jdbcExecElement.getAttribute("batch");
					String paramName = jdbcExecElement.getAttribute("paramName");
					String resultName = jdbcExecElement.getAttribute("resultName");
					String paramNameMap = jdbcExecElement.getAttribute("paramNameMap");
					String singleRow = jdbcExecElement.getAttribute("singleRow");
					String clazz = jdbcExecElement.getAttribute("clazz");
					String sql = jdbcExecElement.getTextContent();
					if (StringUtils.isNotEmpty(paramNameMap)) {
						Map<String, String> map = new HashMap<String, String>();
						for (String pnm : paramNameMap.split(";")) {
							String contextName = pnm.split("=>")[0].trim();
							String ibatisName = pnm.split("=>")[1].trim();
							map.put(contextName, ibatisName);
						}
						bean.getPropertyValues().addPropertyValue("paramNameMap", map);
					}
					if (StringUtils.isNotEmpty(batch)) {
						bean.getPropertyValues().addPropertyValue("batch", Boolean.parseBoolean(batch));
					}
					if (StringUtils.isNotEmpty(singleRow)) {
						bean.getPropertyValues().addPropertyValue("singleRow", Boolean.parseBoolean(singleRow));
					}
					if (StringUtils.isNotEmpty(clazz)) {
						try {
							bean.getPropertyValues().addPropertyValue("clazz", Class.forName(clazz));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
					bean.getPropertyValues().addPropertyValue("paramName",paramName);
					bean.getPropertyValues().addPropertyValue("resultName",resultName);
					bean.getPropertyValues().addPropertyValue("sql",sql);
					jdbcExecBeanList.add(bean);
				}
			}
		}
		beanDef.getPropertyValues().addPropertyValue("jdbcExecBeanList", jdbcExecBeanList);
	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return JdbcProcessor.class;
	}

}

// $Id: JdbcExecProcessorParser.java 12689 2010-05-19 01:01:41Z wei.zhang $