/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

import com.clican.pluto.dataprocess.bean.Deploy;
import com.clican.pluto.dataprocess.engine.DataProcessTransaction;
import com.clican.pluto.dataprocess.engine.DataProcessor;
import com.clican.pluto.dataprocess.engine.ProcessorContainer;
import com.clican.pluto.dataprocess.engine.ProcessorContext;
import com.clican.pluto.dataprocess.enumeration.TransactionMode;
import com.clican.pluto.dataprocess.exception.DataProcessException;

/**
 * 利用Spring Framework来实现ProcessorContainer.
 * <p>
 * 每组Processors都定义在一个Spring Application Context中来作为一组独立的数据处理单元.
 * 
 * @author clican
 * 
 */
public class ProcessorContainerImpl implements ProcessorContainer, ApplicationContextAware {

	private final static Log log = LogFactory.getLog(ProcessorContainerImpl.class);

	private DataProcessTransaction dataProcessTransaction;

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	/**
	 * 每组Processors的部署描述类
	 */
	private List<Deploy> deployList = new ArrayList<Deploy>();

	private List<String> scanList = new ArrayList<String>();

	/**
	 * Spring Application Context的引用,所有的Processors组的Spring Application
	 * Context都会从该context继承下来.
	 */
	private ApplicationContext applicationContext;

	/**
	 * Processor组名和Spring Application Context实例的映射关系
	 */
	private Map<String, ApplicationContext> processorGroupSpringMap = new HashMap<String, ApplicationContext>();

	/**
	 * Processor组名和第一个要执行的Processor的映射关系
	 */
	private Map<String, String> startProcessorNameMap = new HashMap<String, String>();

	public void setDeployList(List<Deploy> deployList) {
		this.deployList = deployList;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setScanList(List<String> scanList) {
		this.scanList = scanList;
	}

	public void setDataProcessTransaction(DataProcessTransaction dataProcessTransaction) {
		this.dataProcessTransaction = dataProcessTransaction;
	}

	/**
	 * 由Spring的init-method属性来配置的启动方法
	 */
	@SuppressWarnings("unchecked")
	public void start() {
		if (log.isInfoEnabled()) {
			log.info("Begin to start Process Data Container");
		}
		for (String scan : scanList) {
			scan = scan.trim();
			try {
				String pattern1;
				String pattern2;
				if (scan.startsWith("file")) {
					pattern1 = scan + "/**/*.xml";
					pattern2 = scan + "/**/*.properties";
				} else {
					pattern1 = ClassUtils.convertClassNameToResourcePath(scan) + "/**/*.xml";
					pattern2 = ClassUtils.convertClassNameToResourcePath(scan) + "/**/*.properties";
				}
				Resource[] resources1 = resourcePatternResolver.getResources(pattern1);
				Resource[] resources2 = resourcePatternResolver.getResources(pattern2);
				Map<String, Resource> map1 = new HashMap<String, Resource>();
				Map<String, Resource> map2 = new HashMap<String, Resource>();
				Set<String> usingPropertyResource = new HashSet<String>();
				for (Resource resource : resources1) {
					map1.put(resource.getFilename().split("\\.")[0], resource);
				}
				for (Resource resource : resources2) {
					String fileName = resource.getFilename().split("\\.")[0];
					String[] fn = fileName.split("\\_");
					if (fn.length == 2 && map1.containsKey(fn[0])) {
						usingPropertyResource.add(fn[0]);
					} else {
						throw new RuntimeException("properties文件[" + fileName + "]命名不规范");
					}
					map2.put(resource.getFilename().split("\\.")[0], resource);
				}
				for (Resource resource1 : resources1) {
					final Resource xmlRes = resource1;

					String fileName1 = resource1.getFilename().split("\\.")[0];
					if (usingPropertyResource.contains(fileName1)) {
						for (Resource resource2 : resources2) {
							AbstractXmlApplicationContext subContext = new AbstractXmlApplicationContext(this.applicationContext) {
								protected Resource[] getConfigResources() {
									return new Resource[] { xmlRes };
								}
							};
							String fileName2 = resource2.getFilename().split("\\.")[0];
							String[] fn = fileName2.split("\\_");
							if (fn[0].equals(fileName1)) {
								com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer parentConf = (com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer) applicationContext
										.getBean("propertyConfigurer");
								Resource[] resources = new Resource[1 + parentConf.getLocations().length];
								for (int i = 0; i < parentConf.getLocations().length; i++) {
									resources[i] = parentConf.getLocations()[i];
								}
								resources[resources.length - 1] = resource2;
								com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer subContainerConf = new com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer();
								subContainerConf.setFileEncoding("utf-8");
								subContainerConf.setLocations(resources);
								subContext.addBeanFactoryPostProcessor(subContainerConf);
								subContext.refresh();
								processorGroupSpringMap.put(fn[1], subContext);
								Collection<String> coll = (Collection<String>) subContext.getBeansOfType(BaseDataProcessor.class).keySet();
								for (String beanName : coll) {
									BaseDataProcessor bean = (BaseDataProcessor) subContext.getBean(beanName);
									if (bean.isStartProcessor()) {
										startProcessorNameMap.put(fn[1], beanName);
										break;
									}
								}
								if (!startProcessorNameMap.containsKey(fn[1])) {
									throw new RuntimeException("启动Process Container错误," + fn[1] + "启动失败");
								}
							}
						}
					} else {
						AbstractXmlApplicationContext subContext = new AbstractXmlApplicationContext(this.applicationContext) {
							protected Resource[] getConfigResources() {
								return new Resource[] { xmlRes };
							}
						};
						subContext.addBeanFactoryPostProcessor((BeanFactoryPostProcessor) applicationContext.getBean("propertyConfigurer"));
						subContext.refresh();
						processorGroupSpringMap.put(fileName1, subContext);
						Collection<String> coll = (Collection<String>) subContext.getBeansOfType(BaseDataProcessor.class).keySet();
						for (String beanName : coll) {
							BaseDataProcessor bean = (BaseDataProcessor) subContext.getBean(beanName);
							if (bean.isStartProcessor()) {
								startProcessorNameMap.put(fileName1, beanName);
								break;
							}
						}
						if (!startProcessorNameMap.containsKey(fileName1)) {
							throw new RuntimeException("启动Process Container错误," + fileName1 + "启动失败");
						}
					}

				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (Deploy deploy : deployList) {
			try {
				ClassPathXmlApplicationContext subContext = new ClassPathXmlApplicationContext(new String[] { deploy.getUrl() }, this.applicationContext);
				if (StringUtils.isNotEmpty(deploy.getPropertyResources())) {
					com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer parentConf = (com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer) applicationContext
							.getBean("propertyConfigurer");

					String[] propertyResources = deploy.getPropertyResources().split(",");
					Resource[] resources = new Resource[propertyResources.length + parentConf.getLocations().length];
					for (int i = 0; i < parentConf.getLocations().length; i++) {
						resources[i] = parentConf.getLocations()[i];
					}
					for (int i = parentConf.getLocations().length; i < resources.length; i++) {
						String propertyResource = propertyResources[i - parentConf.getLocations().length];
						if (propertyResource.startsWith("classpath")) {
							resources[i] = new ClassPathResource(propertyResource.substring(propertyResource.indexOf(":") + 1));
						} else {
							resources[i] = new FileSystemResource(propertyResource.substring(propertyResource.indexOf(":") + 1));
						}

					}

					com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer subContainerConf = new com.clican.pluto.common.support.spring.PropertyPlaceholderConfigurer();
					subContainerConf.setFileEncoding("utf-8");
					subContainerConf.setLocations(resources);
					subContext.addBeanFactoryPostProcessor(subContainerConf);
				} else {
					subContext.addBeanFactoryPostProcessor((BeanFactoryPostProcessor) applicationContext.getBean("propertyConfigurer"));
				}
				subContext.refresh();
				processorGroupSpringMap.put(deploy.getName(), subContext);
				Collection<String> coll = (Collection<String>) subContext.getBeansOfType(BaseDataProcessor.class).keySet();
				for (String beanName : coll) {
					BaseDataProcessor bean = (BaseDataProcessor) subContext.getBean(beanName);
					if (bean.isStartProcessor()) {
						startProcessorNameMap.put(deploy.getName(), beanName);
						break;
					}
				}
				if (!startProcessorNameMap.containsKey(deploy.getName())) {
					throw new RuntimeException("启动Process Container错误," + deploy.getName() + "启动失败");
				}
			} catch (Exception e) {
				log.error("Depoly [" + deploy.getName() + "] failure", e);
				throw new RuntimeException(e);
			}
		}

		log.info("The Process Data Container has been started successfully.");
	}

	
	public void processData(String processorGroupName) throws DataProcessException {
		this.processData(processorGroupName, null);
	}

	/**
	 * @see ProcessorContainer#processData(String, ProcessorContext)
	 */
	
	public void processData(String processorGroupName, ProcessorContext context) throws DataProcessException {
		if (log.isDebugEnabled()) {
			log.debug("begin to invoke process [" + processorGroupName + "]");
		}
		ApplicationContext applicationContext = processorGroupSpringMap.get(processorGroupName);
		if (context == null) {
			context = new ProcessorContextImpl();
		}
		context.setProcessorGroupName(processorGroupName);
		String startProcessorName = startProcessorNameMap.get(processorGroupName);

		DataProcessor processor = (DataProcessor) applicationContext.getBean(startProcessorName);
		try {
			String transaction = processor.getTransaction();
			TransactionMode mode = TransactionMode.convert(transaction);
			if (mode == TransactionMode.BEGIN) {
				DataProcessor dp = this.dataProcessTransaction.doInCommit(processor, context);
				if (dp != null) {
					dp.afterProcess(context);
				}
			} else {
				processor.beforeProcess(context);
				processor.process(context);
				processor.afterProcess(context);
			}
		} catch (DataProcessException e) {
			throw new DataProcessException("processorName[" + processorGroupName + "]", e);
		} catch (Throwable e) {
			throw new DataProcessException("processorName[" + processorGroupName + "]", e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Invoking process [" + processorGroupName + "] is finished.");
		}
	}

}

// $Id: ProcessorContainerImpl.java 15359 2010-06-25 05:09:46Z wei.zhang $