package com.clican.appletv.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.core.service.localserver.LocalServerService;

@Controller
public class LocalServerController {

	private final static Log log = LogFactory
			.getLog(LocalServerController.class);
	@Autowired
	private LocalServerService localServerService;

	@RequestMapping("/localserver/register.do")
	public void registerLocalServer(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("innerIP") String innerIP) {
		String outerIP = request.getRemoteAddr();
		if (log.isDebugEnabled()) {
			log.debug("register local server, outerIP:" + outerIP
					+ ", innerIP:" + innerIP);
		}
	}

	@RequestMapping("/localserver/retrive.do")
	public void retriveLocalServer(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String outerIP = request.getRemoteAddr();
		String innerIP = localServerService.retriveLocalServer(outerIP);
		if (log.isDebugEnabled()) {
			log.debug("retrive local server, outerIP:" + outerIP + ", innerIP:"
					+ innerIP);
		}
		if (StringUtils.isNotEmpty(innerIP)) {
			response.getOutputStream().write(innerIP.getBytes("utf-8"));
		} else {
			response.getOutputStream().write("N/A".getBytes("utf-8"));
		}
	}
}
