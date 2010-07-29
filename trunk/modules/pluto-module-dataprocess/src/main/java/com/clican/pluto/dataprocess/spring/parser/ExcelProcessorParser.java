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
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.clican.pluto.dataprocess.bean.ExcelExecBean;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.processes.ExcelProcessor;

/**
 * excel元素的解析处理
 *
 * @author wei.zhang
 *
 */
public class ExcelProcessorParser extends AbstractProcessorParser {

	@SuppressWarnings("unchecked")
	
	public void customiseBeanDefinition(BeanDefinition beanDef, Element element, ParserContext parserContext) {
		List excelBeanList = new ManagedList();
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String localName = node.getLocalName();
				if ("write".equals(localName) || "read".equals(localName)) {
					RootBeanDefinition bean = new RootBeanDefinition();
					bean.setAbstract(false);
					bean.setBeanClass(ExcelExecBean.class);
					bean.setLazyInit(false);
					bean.setAutowireMode(Autowire.BY_NAME.value());
					Element paramElement = (Element) node;
					String paramName = paramElement.getAttribute("paramName");
					String resultName = paramElement.getAttribute("resultName");
					String resource = paramElement.getAttribute("resource");
					String sheetName = paramElement.getAttribute("sheetName");
					String columns = paramElement.getAttribute("columns");
					String columnsVarName = paramElement.getAttribute("columnsVarName");
					String sheetVarName = paramElement.getAttribute("sheetVarName");
					String typeMapStr = paramElement.getAttribute("typeMap");
					String resourceVarName=paramElement.getAttribute("resourceVarName");
					Map<String,String>  typeMap = new HashMap<String,String>();
					if ("read".equals(localName)) {
						for(String type : typeMapStr.split(";")){
							typeMap.put(type.split("=>")[0], type.split("=>")[1]);
						}
					}
					bean.getPropertyValues().addPropertyValue("paramName", paramName);
					bean.getPropertyValues().addPropertyValue("resultName", resultName);
					bean.getPropertyValues().addPropertyValue("sheetName", sheetName);
					if(StringUtils.isNotEmpty(columns)){
						bean.getPropertyValues().addPropertyValue("columns", columns.split(","));
					}
					bean.getPropertyValues().addPropertyValue("resource", resource);
					bean.getPropertyValues().addPropertyValue("typeMap", typeMap);
					bean.getPropertyValues().addPropertyValue("columnsVarName", columnsVarName);
					bean.getPropertyValues().addPropertyValue("sheetVarName", sheetVarName);
					bean.getPropertyValues().addPropertyValue("resourceVarName", resourceVarName);
					if("write".equals(localName)){
						bean.getPropertyValues().addPropertyValue("read", false);
					}
					excelBeanList.add(bean);
				}
			}
		}
		beanDef.getPropertyValues().addPropertyValue("excelExecBeanList", excelBeanList);

	}

	
	public Class<? extends DataProcessor> getDataProcessorClass() {
		return ExcelProcessor.class;
	}

}

// $Id: ExcelProcessorParser.java 12604 2010-05-18 00:38:19Z wei.zhang $