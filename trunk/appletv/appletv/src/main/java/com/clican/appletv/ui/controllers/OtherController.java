package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.tudou.TudouClient;
import com.clican.appletv.core.service.tudou.TudouClientImpl;

@Controller
public class OtherController {

	private final static Log log = LogFactory.getLog(OtherController.class);

	@Autowired
	private SpringProperty springProperty;

	@Autowired
	private TudouClient tudouClient;

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

	@RequestMapping("/showxml.xml")
	public void showXmlPage(HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String xml = (String)request.getSession().getAttribute("simulate.xml");
		response.setContentType("text/xml;charset=utf-8");
		OutputStream os = response.getOutputStream();
		os.write(xml.getBytes("utf-8"));
		os.close();
	}
	
	@RequestMapping("/postxml.xml")
	public void postXmlPage(HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		String xml = this.getContent(request);
		request.getSession().setAttribute("simulate.xml", xml);
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

	@RequestMapping("/xunlei/getsession.do")
	public void getXunleiSession(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String value = "{\"userid\":\"5663595\",\"sessionid\":\"75F30341DD84F450A07B5F048941BDA785F6AC3A12F9A04ADD949849CD339CC26B882B5F062969E7F71BF99995D9719814CB2E481F15545E89D85D6161F9649A\"}";
		response.getOutputStream().write(value.getBytes("utf-8"));
	}

	@RequestMapping("/xunlei/geturl.do")
	public void getXunleiUrl(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String xunleiurl = getContent(request);
		String refer = "http://61.147.76.6/iplay.html";
		Map<String, String> header = new HashMap<String, String>();
		header.put("Referer", refer);
		PostResponse pr = ((TudouClientImpl) tudouClient).httpGetForCookie(
				xunleiurl, header, null);
		response.getOutputStream().write(pr.getContent().getBytes("utf-8"));
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
