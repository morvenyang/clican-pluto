package com.clican.appletv.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForecastController {

	private final static Log log = LogFactory.getLog(ForecastController.class);

	@RequestMapping("/forecast.do")
	public String indexPage(HttpServletRequest request,
			HttpServletResponse response) {
		
		if (log.isDebugEnabled()) {
			log.debug("access forecast index page");
		}

		return "forecast";
	}

}
