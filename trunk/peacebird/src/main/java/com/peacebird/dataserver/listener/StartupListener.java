package com.peacebird.dataserver.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class StartupListener extends ContextLoaderListener implements
		ServletContextListener {

	protected ContextLoader createContextLoader() {
		return new ContextLoader() {
			protected ApplicationContext loadParentContext(
					ServletContext servletContext) throws BeansException {
				ApplicationContext ctx = (new ClassPathXmlApplicationContext(
						new String[] { "classpath*:peacebird-*.xml", }));
				return ctx;
			}
		};
	}
}
