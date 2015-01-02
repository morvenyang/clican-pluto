package com.chinatelecom.xysq.action;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.seam.security.Identity;

import com.chinatelecom.xysq.bean.Constants;
import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.service.AreaService;
import com.chinatelecom.xysq.service.UserService;

public class BaseAction {

	public final static int PAGE_SIZE = 25;
	
	protected int page;
	
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
	protected AreaService getAreaService() {
		return (AreaService) Constants.ctx.getBean("areaService");
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
}
