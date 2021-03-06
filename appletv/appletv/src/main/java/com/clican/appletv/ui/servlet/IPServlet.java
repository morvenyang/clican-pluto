package com.clican.appletv.ui.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.clican.appletv.common.SpringProperty;

public class IPServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3182255622341837551L;

	private final static Log log = LogFactory.getLog(IPServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ApplicationContext ctx = (ApplicationContext) this
				.getServletContext()
				.getAttribute(
						WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		SpringProperty springProperty = (SpringProperty) ctx
				.getBean("springProperty");
		String requestUrl = req.getRequestURI();
		String fileName = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
		String realPath = this.getServletContext().getRealPath("WEB-INF");
		File path = new File(realPath);
		if (fileName.contains("js")) {
			realPath = path.getParentFile().getAbsolutePath() + "/javascript/"
					+ fileName;
		} else {
			realPath = path.getParentFile().getAbsolutePath() + "/" + fileName;
		}

		String content = FileUtils
				.readFileToString(new File(realPath), "utf-8");
		content = content.replaceAll("http://local.clican.org/appletv",
				springProperty.getSystemServerUrl());
		content = content.replaceAll("http://www.clican.org/appletv",
				springProperty.getSystemServerUrl());
		content = content.replaceAll("http://clican.org/appletv",
				springProperty.getSystemServerUrl());
		String ua = req.getHeader("User-Agent");
		if(log.isDebugEnabled()){
			log.debug("User-Agent:"+ua);
		}
		if (StringUtils.isNotEmpty(ua) && ua.contains("Chrome")) {
			if(log.isDebugEnabled()){
				log.debug("replace atv to browser");
			}
			content = content.replaceAll("simulate : 'atv'", "simulate : 'browser'");
			content = content.replaceAll("logEnable : false","logEnable : true");
		}else{
			if(log.isDebugEnabled()){
				log.debug("replace nothing for non-chrome agent");
			}
		}
		resp.getOutputStream().write(content.getBytes("utf-8"));
	}
}
