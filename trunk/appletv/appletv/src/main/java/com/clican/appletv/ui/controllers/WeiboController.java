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

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.model.User;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.weibo.WeiboClient;

@Controller
public class WeiboController {
	private final static Log log = LogFactory.getLog(WeiboController.class);

	@Autowired
	private WeiboClient weiboClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/weibo/oaAuthCallback.do")
	public String callback(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "code", required = false) String code)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("code=" + code);
		}
		String accessToken = new Oauth().getAccessTokenByCode(code)
				.getAccessToken();
		if (log.isDebugEnabled()) {
			log.debug("set access token[" + accessToken + "]");
		}
		request.getSession().setAttribute("accessToken", accessToken);
		return "weibo/oaAuthCallback";
	}

	@RequestMapping("/weibo/bind.do")
	public String bind(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "deviceId", required = false) String deviceId)
			throws Exception {
		String accessToken = (String) request.getSession().getAttribute(
				"accessToken");
		if (log.isDebugEnabled()) {
			log.debug("Bind " + deviceId + " with accessToken " + accessToken);
		}
		boolean result = false;
		if (StringUtils.isNotEmpty(accessToken)) {
			result = weiboClient.saveUserInfo(accessToken, deviceId);
		}
		request.setAttribute("result", result);
		request.setAttribute("weiboLoginURL", springProperty.getWeiboLoginURL());
		return "weibo/bind";
	}

	@RequestMapping("/weibo/checkAccessToken.xml")
	public String checkAccessToken(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "deviceId", required = false) String deviceId)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("check access token is valid or not");
		}
		String uid = weiboClient.getUid(deviceId);
		String accessToken = weiboClient.getAccessToken(deviceId);
		if (StringUtils.isEmpty(uid)) {
			request.getSession().setAttribute("weiboUid", uid);
			request.getSession().setAttribute("weiboAccessToken", accessToken);
			Users users = new Users();
			users.client.setToken(accessToken);
			User user = users.showUserById(uid);
			request.getSession().setAttribute("weiboUser", user);
			return "weibo/profile";
		} else {
			return "weibo/profile";
		}
	}

	@RequestMapping("/weibo/homeTimeline.xml")
	public String homeTimeline(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return "weibo/homeTimeline";
	}
}
