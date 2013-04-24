package com.clican.pluto.cms.ui.ext.security;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("org.jboss.seam.security.identity")
@Scope(ScopeType.SESSION)
@Install(precedence = Install.APPLICATION)
@Startup
public class Identity extends org.jboss.seam.security.Identity {

	/**
     * 
     */
	private static final long serialVersionUID = 5423746160747703118L;

	private String jsessionid;

	@BypassInterceptors
	public String getJsessionid() {
		return jsessionid;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

}
