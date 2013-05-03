package com.clican.pluto.cms.ui.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

import com.clican.pluto.common.bean.SpringProperty;
import com.clican.pluto.common.constant.Constants;

@Scope(ScopeType.APPLICATION)
@Name("IrpApplication")
@Install(precedence = Install.APPLICATION)
@Startup
public class PlutoApplication {

	private final static Log log = LogFactory.getLog(PlutoApplication.class);

	@Out(scope = ScopeType.APPLICATION, required = false)
	private String contextPath = "/pluto";

	@Out(scope = ScopeType.APPLICATION, required = true)
	private SpringProperty springProperty;

	@Create
	public void createApplication() {
		if (log.isInfoEnabled()) {
			log.info("Startup Pluto Application");
		}
		springProperty = (SpringProperty) Constants.ctx.getBean("springProperty");
		if (log.isInfoEnabled()) {
			log.info("contextPath=[" + contextPath + "]");
		}
	}

	public String getContextPath() {
		return contextPath;
	}

	public SpringProperty getSpringProperty() {
		return springProperty;
	}

}
