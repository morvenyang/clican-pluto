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
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
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

	@RequestMapping("/weibo/login.do")
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.sendRedirect(springProperty.getWeiboLoginURL());
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
		if (StringUtils.isNotEmpty(uid)) {
			request.getSession().setAttribute("weiboUid", uid);
			request.getSession().setAttribute("weiboAccessToken", accessToken);
			Users users = new Users();
			users.client.setToken(accessToken);
			User user = users.showUserById(uid);
			request.getSession().setAttribute("weiboUser", user);
			request.setAttribute("serverurl",
					springProperty.getSystemServerUrl());
			return "weibo/profile";
		} else {
			request.setAttribute("weiboLoginURL",
					springProperty.getSystemServerUrl() + "/weibo/login.do");
			request.setAttribute("deviceId", deviceId);
			return "weibo/checkAccessToken";
		}
	}

	@RequestMapping("/weibo/imagePreview.xml")
	public String imagePreview(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "imageURL", required = false) String imageURL)
			throws Exception {
		request.setAttribute("imageURL", imageURL);
		return "weibo/imagePreview";
	}

	@RequestMapping("/weibo/homeTimeline.xml")
	public String homeTimeline(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "index", required = false) Integer index)
			throws Exception {
		Timeline timeline = new Timeline();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		timeline.setToken(accessToken);
		StatusWapper statusWapper = (StatusWapper) request.getSession()
				.getAttribute("weiboStatusWapper");
		Integer weiboPage = 1;
		Status status = null;
		if (statusWapper == null || index == null) {
			statusWapper = timeline.getHomeTimeline(0, 0, new Paging(1, 20));
			if (statusWapper.getStatuses().size() > 0) {
				status = statusWapper.getStatuses().get(0);
			}
			index = 0;
			request.setAttribute("weiboStatus", status);
			request.setAttribute("weiboStatusImageURL",
					weiboClient.generateWeiboImage(status));
			request.getSession().setAttribute("weiboPage", 0);
			request.getSession()
					.setAttribute("weiboStatusWapper", statusWapper);
		} else {
			weiboPage = (Integer) request.getSession()
					.getAttribute("weiboPage");
			if (index == -1) {
				if (weiboPage > 1) {
					weiboPage--;
				}
				statusWapper = timeline.getHomeTimeline(0, 0, new Paging(
						weiboPage, 20, -1,
						(statusWapper.getPreviousCursor() == 0 ? -1
								: statusWapper.getPreviousCursor())));
				request.getSession().setAttribute("weiboStatusWapper",
						statusWapper);
				index = statusWapper.getStatuses().size() - 1;
			} else if (index == statusWapper.getStatuses().size()) {
				weiboPage++;
				statusWapper = timeline.getHomeTimeline(0, 0, new Paging(
						weiboPage, 20, statusWapper.getNextCursor()));
				request.getSession().setAttribute("weiboStatusWapper",
						statusWapper);
				index = 0;
			}
			if (index >= 0 && index < statusWapper.getStatuses().size()) {
				status = statusWapper.getStatuses().get(index);
			}
			request.setAttribute("weiboStatus", status);
			request.setAttribute("weiboStatusImageURL",
					weiboClient.generateWeiboImage(status));
			request.getSession().setAttribute("weiboPage", weiboPage);
		}
		request.setAttribute("prevIndex", index - 1);
		request.setAttribute("nextIndex", index + 1);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "weibo/homeTimeline";
	}

	@RequestMapping("/weibo/homeTimeline2.xml")
	public String homeTimeline2(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false) Integer page,
			Long sinceId, Long maxId) throws Exception {
		Timeline timeline = new Timeline();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		timeline.setToken(accessToken);
		if (page == null) {
			page = 1;
		}
		if (page < 1) {
			page = 1;
		}
		Paging paging = new Paging();
		paging.setCount(50);
		if (sinceId != null && sinceId >= 0) {
			paging.setSinceId(sinceId);
		}
		if (maxId != null && maxId >= 0) {
			paging.setMaxId(maxId);
		}

		StatusWapper statusWapper = timeline.getHomeTimeline(0, 0, paging);

		for (Status status : statusWapper.getStatuses()) {
			String statusPic = weiboClient.generateWeiboImage(status);
			status.setStatusPic(statusPic);
		}
		if (statusWapper.getStatuses().size() > 0) {
			request.setAttribute("weiboFirstStatus", statusWapper.getStatuses()
					.get(0));
		} else {
			request.setAttribute("weiboFirstStatus", null);
		}
		request.setAttribute("weiboStatusWapper", statusWapper);
		request.setAttribute("page", page);
		if (statusWapper.getStatuses().size() > 0) {
			Status prevOne = statusWapper.getStatuses().get(0);
			Status nextOne = statusWapper.getStatuses().get(
					statusWapper.getStatuses().size() - 1);
			request.setAttribute("sinceId", prevOne.getIdstr());
			request.setAttribute("maxId", nextOne.getIdstr() - 1);
		} else {
			request.setAttribute("sinceId", 0);
			request.setAttribute("maxId", 0);
		}
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "weibo/homeTimeline2";
	}
}
