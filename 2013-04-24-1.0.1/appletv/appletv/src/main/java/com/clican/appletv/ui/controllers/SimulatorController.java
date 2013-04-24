package com.clican.appletv.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;

@Controller
public class SimulatorController {

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/simulator/input.xml")
	public String inputPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback) {
		request.setAttribute("callback", callback);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "simulator/input";
	}
}
