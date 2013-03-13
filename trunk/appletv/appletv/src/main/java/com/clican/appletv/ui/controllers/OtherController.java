package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;

@Controller
public class OtherController {

	private final static Log log = LogFactory.getLog(OtherController.class);

	@Autowired
	private SpringProperty springProperty;

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

	@RequestMapping("/newpage.xml")
	public void newPage(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("xml") String xml)
			throws IOException {
		response.setContentType("text/xml;charset=utf-8");
		OutputStream os = response.getOutputStream();
		os.write(xml.getBytes("utf-8"));
		os.close();
	}

	@RequestMapping("/jstest.do")
	public String jstest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "jstest";
	}

	@RequestMapping("/setValue.do")
	public void setValue(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String content = this.getContent(request);
		JSONObject json = JSONObject.fromObject(content);
		String name = json.getString("name");
		String value = json.getJSONObject("value").toString();
		request.getSession().setAttribute(name, value);
	}

	@RequestMapping("/getValue.do")
	public void getValue(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String value = (String) request.getSession().getAttribute(
				request.getParameter("name"));
		if (StringUtils.isNotEmpty(value)) {
			response.getOutputStream().write(value.getBytes("utf-8"));
		}
	}

	private String getContent(HttpServletRequest request) throws Exception {
		InputStream is = request.getInputStream();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
		String content = new String(os.toByteArray(), "UTF-8");
		is.close();
		os.close();
		return content;
	}
}
