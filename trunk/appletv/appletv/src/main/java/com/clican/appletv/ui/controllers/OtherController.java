package com.clican.appletv.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clican.appletv.common.SpringProperty;

@Controller
public class OtherController {
	
	@Autowired
	private SpringProperty springProperty;
	
	@RequestMapping("/releasenote.xml")
	public String releasenote(HttpServletRequest request,
			HttpServletResponse response){
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "releasenote";
	}
}
