package com.clican.appletv.ui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WeiboController {
	private final static Log log = LogFactory.getLog(WeiboController.class);
	
	@RequestMapping("/weibo/oaAuthCallback.do")
	public void callback(HttpServletRequest request) {
		String code= request.getParameter("code");
		log.debug("code="+code);
	}
}
