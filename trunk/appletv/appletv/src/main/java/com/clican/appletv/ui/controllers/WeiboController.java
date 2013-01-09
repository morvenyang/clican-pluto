package com.clican.appletv.ui.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WeiboController {

	private final static Log log = LogFactory.getLog(TudouController.class);
	
	@RequestMapping("/weibo/oaAuthCallback.do")
	public String oaAuthCallback() throws Exception{
		return "weibo/oaAuthCallback";
	}
}
