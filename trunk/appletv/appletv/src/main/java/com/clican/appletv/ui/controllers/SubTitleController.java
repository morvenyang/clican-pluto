package com.clican.appletv.ui.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.core.service.subtitle.SubTitleClient;

@Controller
public class SubTitleController {

	@Autowired
	private SubTitleClient subTitleClient;
	
	public void downloadSubTitle(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "url", required = true) String url)
			throws IOException {
		String content = subTitleClient.downloadSubTitle(url);
		if(StringUtils.isNotEmpty(content)){
			response.setContentType("text/plain;charset=utf-8");
			response.getOutputStream().write(content.getBytes("utf-8"));
		}
	}
}
