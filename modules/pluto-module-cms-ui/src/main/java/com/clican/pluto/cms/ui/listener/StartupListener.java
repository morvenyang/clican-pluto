/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.ui.listener;

import java.io.File;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.clican.pluto.cms.core.service.DataStructureService;
import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.util.JndiUtils;
import com.clican.pluto.common.util.MockContextFactory;
import com.clican.pluto.transaction.resources.memory.XAFileSetResourceMemoryImpl;

public class StartupListener extends ContextLoaderListener implements
		ServletContextListener {

	private final static Log log = LogFactory.getLog(StartupListener.class);

	public void contextDestroyed(ServletContextEvent event) {

	}

	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		Constants.DATA_STRUCTURE_XHTML_FOLDER = new File(event
				.getServletContext().getRealPath("datastructure"));
		try {
			Velocity.init(Thread.currentThread().getContextClassLoader()
					.getResource("velocity.properties").getPath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ApplicationContext ctx = null;
		JndiUtils.setJndiFactory(MockContextFactory.class.getName());
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(Context.INITIAL_CONTEXT_FACTORY, MockContextFactory.class
				.getName());
		try {
			ctx = (ApplicationContext) JndiUtils
					.lookupObject(ApplicationContext.class.getName());
			if (ctx == null) {
				log.warn("Cannot get ApplicationContext from JNDI");
			}
		} catch (Exception e) {
			log.warn("Cannot get ApplicationContext from JNDI");
		}
		if (ctx == null) {
			ctx = (new ClassPathXmlApplicationContext(
					new String[] { "classpath*:META-INF/ui-*.xml", }));
		}

		XmlWebApplicationContext wac = (XmlWebApplicationContext) WebApplicationContextUtils
				.getRequiredWebApplicationContext(event.getServletContext());
		wac.setParent(ctx);
		wac.refresh();
		event.getServletContext().setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				wac);
		Constants.ctx = wac;
		XAFileSetResourceMemoryImpl dataStructureXHTMLResource = null;
		dataStructureXHTMLResource = (XAFileSetResourceMemoryImpl) wac
				.getBean("dataStructureXHTMLResource");
		dataStructureXHTMLResource
				.setDirectory(Constants.DATA_STRUCTURE_XHTML_FOLDER);
		DataStructureService dataStructureService = (DataStructureService) wac
				.getBean("dataStructureService");
		dataStructureService.init();
		UserTransaction userTransaction = (UserTransaction) wac
				.getBean("userTransaction");
		JndiUtils.bind("userTransaction", userTransaction);
	}
}

// $Id$