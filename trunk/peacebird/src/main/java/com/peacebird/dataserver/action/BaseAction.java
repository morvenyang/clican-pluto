package com.peacebird.dataserver.action;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.security.Identity;

import com.peacebird.dataserver.bean.Constants;
import com.peacebird.dataserver.bean.SpringProperty;
import com.peacebird.dataserver.service.DataService;
import com.peacebird.dataserver.service.UserService;

public class BaseAction {

	protected Identity getIdentity() {
		HttpSession session = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getSession();
		Identity identity = (Identity) session
				.getAttribute("org.jboss.seam.security.identity");
		return identity;
	}

	protected SpringProperty getSpringProperty() {
		return (SpringProperty) Constants.ctx.getBean("springProperty");
	}

	protected UserService getUserService() {
		return (UserService) Constants.ctx.getBean("userService");
	}
	
	protected DataService getDataService() {
		return (DataService) Constants.ctx.getBean("dataService");
	}
}
