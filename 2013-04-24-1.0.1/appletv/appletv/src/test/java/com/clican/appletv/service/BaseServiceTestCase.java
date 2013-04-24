package com.clican.appletv.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class BaseServiceTestCase extends AbstractDependencyInjectionSpringContextTests {

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath*:META-INF/test-*.xml" };
    }

}
