package com.chinatelecom.xysq.action;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinatelecom.xysq.bean.Constants;
import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.service.AreaService;
import com.chinatelecom.xysq.service.BroadbandRemindService;
import com.chinatelecom.xysq.service.PosterService;
import com.chinatelecom.xysq.service.StoreService;
import com.chinatelecom.xysq.service.UserService;

public class BaseAction {

	protected final Log log = LogFactory.getLog(this.getClass());

	public com.chinatelecom.xysq.bean.Identity getIdentity() {
		HttpSession session = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getSession();
		com.chinatelecom.xysq.bean.Identity identity = (com.chinatelecom.xysq.bean.Identity) session
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

	protected BroadbandRemindService getBroadbandRemindService() {
		return (BroadbandRemindService) Constants.ctx.getBean("broadbandRemindService");
	}
	
	protected StoreService getStoreService() {
		return (StoreService) Constants.ctx.getBean("storeService");
	}
	
	protected PosterService getPosterService() {
		return (PosterService) Constants.ctx.getBean("posterService");
	}

}
