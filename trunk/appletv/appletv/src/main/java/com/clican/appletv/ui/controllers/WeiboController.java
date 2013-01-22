package com.clican.appletv.ui.controllers;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
import weibo4j.Favorite;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Favorites;
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
		String uid = (String) request.getSession().getAttribute("weiboUid");
		if (StringUtils.isEmpty(uid)) {
			uid = weiboClient.getUid(deviceId);
		}

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

	@RequestMapping("/weibo/homeTimelineBar.xml")
	public String homeTimelineBar(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "weibo/homeTimelineBar";
	}

	@RequestMapping("/weibo/favorite.xml")
	public String favorite(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false) Integer page)
			throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		Favorite favorite = new Favorite();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		favorite.setToken(accessToken);
		if (page == null || page < 1) {
			page = 1;
		}
		Paging paging = new Paging(page, 10);
		List<Favorites> favorites = favorite.getFavorites(paging);
		List<Status> statuses = new ArrayList<Status>();
		for (Favorites f : favorites) {
			statuses.add(f.getStatus());
		}
		StatusWapper statusWapper = new StatusWapper(statuses);

		weiboClient.processLongUrl(statusWapper, accessToken);
		for (Status status : statusWapper.getStatuses()) {
			String text = status.getText();
			if (status.getRetweetedStatus() != null) {
				if (status.getRetweetedStatus().getUser() != null) {
					text += " @"
							+ status.getRetweetedStatus().getUser()
									.getScreenName();
				}
				if (StringUtils.isNotEmpty(status.getRetweetedStatus()
						.getText())) {
					text += " " + status.getRetweetedStatus().getText();
				}
			}
			status.setFullText(text);
			status.setFullTextEncode(URLEncoder.encode(text, "utf-8"));
		}
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("weiboStatusWapper", statusWapper);
		request.setAttribute("weiboPage", page);
		return "/weibo/favorite";
	}

	@RequestMapping("/weibo/homeTimeline.xml")
	public String homeTimeline(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "feature", required = false) Integer feature,
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
		if (feature == null) {
			feature = 0;
		}
		request.setAttribute("weiboFeature", feature);
		StatusWapper statusWapper = timeline
				.getHomeTimeline(0, feature, paging);
		weiboClient.processLongUrl(statusWapper, accessToken);
		for (Status status : statusWapper.getStatuses()) {
			String text = status.getText();
			if (status.getRetweetedStatus() != null) {
				if (status.getRetweetedStatus().getUser() != null) {
					text += " @"
							+ status.getRetweetedStatus().getUser()
									.getScreenName();
				}
				if (StringUtils.isNotEmpty(status.getRetweetedStatus()
						.getText())) {
					text += " " + status.getRetweetedStatus().getText();
				}
			}
			status.setFullText(text);
			status.setFullTextEncode(URLEncoder.encode(text, "utf-8"));
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
		request.setAttribute("weiboStatusWapper", statusWapper);

		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "weibo/homeTimeline";
	}

	@RequestMapping("/weibo/textPreview.xml")
	public String textPreview(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "statusId", required = false) Long statusId,
			@RequestParam(value = "fullText", required = false) String fullText)
			throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		request.setAttribute("fullText", fullText);
		request.setAttribute("statusId", statusId);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
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
		String statusContent = "我正在Apple TV3上观看在线视频（" + title
				+ "）@Clican 了解更多 >>>";
		if (StringUtils.isNotEmpty(shareURL)) {
			statusContent += shareURL;
		}
		Status status = null;
		boolean result = true;
		try {
			// status = timeline.UpdateStatus(statusContent);
			status = timeline.UploadStatus(statusContent, imageURL);
		} catch (Exception e) {
			log.error("", e);
			result = false;
		}

		if (status == null) {
			result = false;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("The user["
						+ request.getSession().getAttribute("weiboUid")
						+ "] send status[" + status.getId() + "]");
			}
		}
		if (result) {
			request.setAttribute("promptTitle", "分享成功");
		} else {
			request.setAttribute("promptTitle", "分享失败请稍候再试");
		}

		request.setAttribute("promptDescription", "");
		return "/weibo/prompt";
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

	@RequestMapping("/weibo/doRepost.xml")
	public String doRepost(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "statusId") Long statusId) throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}

		Timeline timeline = new Timeline();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		timeline.setToken(accessToken);
		timeline.Repost(statusId.toString());
		boolean result = true;
		Status status = null;
		try {
			status = timeline.Repost(statusId.toString());
		} catch (Exception e) {
			log.error("", e);
			result = false;
		}
		if (status == null) {
			result = false;
		}
		if (result) {
			request.setAttribute("promptTitle", "转发成功");
		} else {
			request.setAttribute("promptTitle", "转发失败请稍候再试");
		}

		request.setAttribute("promptDescription", "");
		return "/weibo/prompt";
	}

	@RequestMapping("/weibo/doFavorite.xml")
	public String doFavorite(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "statusId") Long statusId) throws Exception {
		if (!isLogin(request, response)) {
			return null;
		}
		Favorite favorite = new Favorite();
		String accessToken = (String) request.getSession().getAttribute(
				"weiboAccessToken");
		favorite.setToken(accessToken);
		boolean result = true;
		Favorites favorites = null;
		try {
			favorites = favorite.createFavorites(statusId.toString());
		} catch (Exception e) {
			log.error("", e);
			result = false;
		}
		if (favorites == null) {
			result = false;
		}
		if (result) {
			request.setAttribute("promptTitle", "收藏成功");
		} else {
			request.setAttribute("promptTitle", "收藏失败请稍候再试");
		}

		request.setAttribute("promptDescription", "");
		return "/weibo/prompt";
	}

}
