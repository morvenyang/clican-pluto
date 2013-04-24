package com.clican.pluto.cms.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.jmock.Mockery;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class BaseTestCase extends
		AbstractDependencyInjectionSpringContextTests {

	protected Log log = LogFactory.getLog(getClass());

	protected Mockery context = new Mockery();

	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:META-INF/cms-core-*.xml" };
	}

	public BaseTestCase() {
		try {
			Velocity.init(Thread.currentThread().getContextClassLoader()
					.getResource("velocity.properties").getPath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}

// $Id$