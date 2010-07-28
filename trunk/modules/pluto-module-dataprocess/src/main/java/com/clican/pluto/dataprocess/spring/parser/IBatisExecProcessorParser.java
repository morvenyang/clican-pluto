/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.dataprocess.spring.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.clican.pluto.dataprocess.bean.IBatisExecBean;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.IBatisExecProcessor;
import com.clican.pluto.dataprocess.enumeration.IBatisExecType;

/**
 * ibatis元素的解析处理
 *
 * @author wei.zhang
 *
 */
public class IBatisExecProcessorParser extends AbstractProcessorParser {

	@Override
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		String sqlMapClient = element.getAttribute("sqlMapClient");
		beanDef.getPropertyValues().addPropertyValue("sqlMapClient", new RuntimeBeanReference(sqlMapClient));

		List<IBatisExecBean> ibatisExecBeanList = new ArrayList<IBatisExecBean>();
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = node.getLocalName();
				if ("exec".equals(localName)) {
					IBatisExecBean bean = new IBatisExecBean();
					Element ibatisExecElement = (Element) node;
					String batch = ibatisExecElement.getAttribute("batch");
					String statement = ibatisExecElement.getAttribute("statement");
					String insertStatement = ibatisExecElement.getAttribute("insertStatement");
					String updateStatement = ibatisExecElement.getAttribute("updateStatement");
					String ibatisExecType = ibatisExecElement.getAttribute("ibatisExecType");
					String paramName = ibatisExecElement.getAttribute("paramName");
					String keyProp = ibatisExecElement.getAttribute("keyProp");
					String valueProp = ibatisExecElement.getAttribute("valueProp");
					String resultName = ibatisExecElement.getAttribute("resultName");
					String paramNameMap = ibatisExecElement.getAttribute("paramNameMap");
					if (StringUtils.isNotEmpty(paramNameMap)) {
						Map<String, String> map = new HashMap<String, String>();
						for (String pnm : paramNameMap.split(";")) {
							String contextName = pnm.split("=>")[0].trim();
							String ibatisName = pnm.split("=>")[1].trim();
							map.put(contextName, ibatisName);
						}
						bean.setParamNameMap(map);
					}
					if (StringUtils.isNotEmpty(batch)) {
						bean.setBatch(Boolean.parseBoolean(batch));
					}

					bean.setStatement(statement);
					bean.setInsertStatement(insertStatement);
					bean.setUpdateStatement(updateStatement);
					bean.setIbatisExecType(IBatisExecType.convert(ibatisExecType));
					bean.setKeyProp(keyProp);
					bean.setValueProp(valueProp);
					bean.setParamName(paramName);
					bean.setResultName(resultName);
					ibatisExecBeanList.add(bean);
				}
			}
		}
		beanDef.getPropertyValues().addPropertyValue("ibatisExecBeanList", ibatisExecBeanList);
	}

	@Override
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return IBatisExecProcessor.class;
	}

}

// $Id: IBatisExecProcessorParser.java 12414 2010-05-13 07:04:18Z wei.zhang $