package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clican.appletv.common.SpringProperty;

@Controller
public class OtherController {
	
	private final static Log log = LogFactory.getLog(OtherController.class);
	
	@Autowired
	private SpringProperty springProperty;
	
	@RequestMapping("/releasenote.xml")
	public String releasenote(HttpServletRequest request,
			HttpServletResponse response){
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "releasenote";
	}
	
	@RequestMapping("/log.do")
	public void logText(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		InputStream is = request.getInputStream();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
		String logText = new String(os.toByteArray(), "UTF-8");
		is.close();
		os.close();
		if (log.isDebugEnabled()) {
			log.debug("log=" + logText);
		}
	}
}
