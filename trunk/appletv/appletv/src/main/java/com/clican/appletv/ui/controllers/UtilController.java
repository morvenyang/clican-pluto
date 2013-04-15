package com.clican.appletv.ui.controllers;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UtilController {

	@RequestMapping("/util/encode.do")
	public void encode(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "encoding", required = false) String encoding)
			throws Exception {
		String c = URLEncoder.encode(content, "GBK");
		response.getOutputStream().write(c.getBytes());
	}
}
