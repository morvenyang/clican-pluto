package com.clican.appletv.ui.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import weibo4j.Comments;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
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

	private boolean isLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (request.getSession().getAttribute("weiboAccessToken") != null) {
			return true;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("The user has not login weibo account.");
			}
			response.sendRedirect(request.getContextPath() + "/releasenote.xml");
			return false;
		}
	}

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
			if (log.isDebugEnabled()) {
				log.debug("There is an user [" + uid
						+ "] begin to play with weibo at ATV3");
			}
			return "weibo/profile";
		} else {
			request.setAttribute("weiboLoginURL",
					springProperty.getSystemServerUrl() + "/weibo/login.do");
			request.setAttribute("deviceId", deviceId);
			return "weibo/checkAccessToken";
		}
	}

	@RequestMapping("/weibo/homeTimeline.xml")
	public String homeTimeline(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "sinceId", required = false) Long sinceId,
			@RequestParam(value = "maxId", required = false) Long maxId)
			throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		Timeline timeline = new Timeline();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		timeline.setToken(accessToken);
		Paging paging = new Paging();
		paging.setCount(10);
		if (sinceId != null && sinceId >= 0) {
			paging.setSinceId(sinceId);
		}
		if (maxId != null && maxId >= 0) {
			paging.setMaxId(maxId);
		}

		StatusWapper statusWapper = timeline.getHomeTimeline(0, 0, paging);
		Map<Long, Status> statusMap = new HashMap<Long, Status>();
		for (Status status : statusWapper.getStatuses()) {
			statusMap.put(status.getIdstr(), status);
		}
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
		request.getSession().setAttribute("weiboStatusMap", statusMap);
		request.setAttribute("weiboStatusWapper", statusWapper);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "weibo/homeTimeline";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/weibo/imagePreview.xml")
	public String imagePreview(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "statusId", required = false) Long statusId)
			throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		Map<Long, Status> statusMap = (Map<Long, Status>) request.getSession()
				.getAttribute("weiboStatusMap");
		Status status = statusMap.get(statusId);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("weiboStatus", status);
		if (StringUtils.isEmpty(status.getOriginalPic())
				&& (status.getRetweetedStatus() == null || StringUtils
						.isEmpty(status.getRetweetedStatus().getOriginalPic()))) {
			return "weibo/textPreview";
		} else {
			return "weibo/imagePreview";
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/weibo/textPreview.xml")
	public String textPreview(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "statusId", required = false) Long statusId)
			throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		Map<Long, Status> statusMap = (Map<Long, Status>) request.getSession()
				.getAttribute("weiboStatusMap");
		Status status = statusMap.get(statusId);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("weiboStatus", status);
		return "weibo/textPreview";
	}

	@RequestMapping("/weibo/showComments.xml")
	public String showComments(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "statusId", required = false) Long statusId,
			@RequestParam(value = "sinceId", required = false) Long sinceId,
			@RequestParam(value = "maxId", required = false) Long maxId)
			throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		Comments comments = new Comments();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		comments.setToken(accessToken);
		Paging paging = new Paging();
		paging.setCount(10);
		if (sinceId != null && sinceId >= 0) {
			paging.setSinceId(sinceId);
		}
		if (maxId != null && maxId >= 0) {
			paging.setMaxId(maxId);
		}

		comments.getCommentById(statusId.toString(), paging, 0);
		CommentWapper commentWapper = comments.getCommentById(
				statusId.toString(), paging, 0);

		if (commentWapper.getComments().size() > 0) {
			Comment prevOne = commentWapper.getComments().get(0);
			Comment nextOne = commentWapper.getComments().get(
					commentWapper.getComments().size() - 1);
			request.setAttribute("sinceId", prevOne.getId());
			request.setAttribute("maxId", nextOne.getId() - 1);
		} else {
			request.setAttribute("sinceId", 0);
			request.setAttribute("maxId", 0);
		}
		request.setAttribute("statusId", statusId);
		request.setAttribute("weiboCommentWapper", commentWapper);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "weibo/showComments";
	}

	@RequestMapping("/weibo/createStatus.xml")
	public String createStatus(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "shareURL", required = false) String shareURL,
			@RequestParam(value = "imageURL", required = false) String imageURL)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("create status, title:" + title + ",shareURL:" + shareURL
					+ ".imageURL:" + imageURL);
		}
		if (!isLogin(request, response)) {
			return null;
		}
		Timeline timeline = new Timeline();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		timeline.setToken(accessToken);
		String statusContent = "我正在Apple TV3上观看在线视频（" + title + "） >>>"
				+ shareURL;
		Status status = null;
		boolean result = true;
		try {
			status = timeline.UploadStatus(statusContent, imageURL);
		} catch (Exception e) {
			log.error("", e);
			result = false;
		}

		if (status == null) {
			result = false;
		}
		request.setAttribute("result", result);
		return "/weibo/createStatus";
	}

	@RequestMapping("/weibo/createComment.xml")
	public String showComments(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "statusId", required = false) Long statusId,
			@RequestParam(value = "comment", required = false) String comment)
			throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		Comments comments = new Comments();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		comments.setToken(accessToken);
		comments.createComment(comment, statusId.toString());
		Paging paging = new Paging();
		paging.setCount(10);
		CommentWapper commentWapper = comments.getCommentById(
				statusId.toString(), paging, 0);
		request.setAttribute("statusId", statusId);
		request.setAttribute("weiboCommentWapper", commentWapper);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "weibo/showComments";
	}
}
