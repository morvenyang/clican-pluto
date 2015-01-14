package com.chinatelecom.xysq.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import com.chinatelecom.xysq.bean.Constants;
import com.chinatelecom.xysq.bean.SpringProperty;

@Scope(ScopeType.APPLICATION)
@Name("xysqApplication")
@Install(precedence = Install.APPLICATION)
@Startup
public class XysqApplication {

	private final static Log log = LogFactory.getLog(XysqApplication.class);

    @Out(scope = ScopeType.APPLICATION, required = true)
    private SpringProperty springProperty;

    @Out(scope = ScopeType.APPLICATION, required = false)
    private String contextPath = "/xysq";

    

    @Create
    public void createApplication() {
        if (log.isInfoEnabled()) {
            log.info("Startup Quiz Application");
        }
        springProperty = (SpringProperty) Constants.ctx.getBean("springProperty");
        springProperty.setContextPath(contextPath);
        if (log.isInfoEnabled()) {
            log.info("contextPath=[" + contextPath + "]");
        }
    }

    public SpringProperty getSpringProperty() {
        return springProperty;
    }

    public String getContextPath() {
        return contextPath;
    }

    

   
}
