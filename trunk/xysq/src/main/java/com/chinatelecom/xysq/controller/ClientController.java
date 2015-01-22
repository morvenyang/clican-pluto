package com.chinatelecom.xysq.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.json.LoginJson;
import com.chinatelecom.xysq.json.RegisterJson;
import com.chinatelecom.xysq.service.AreaService;
import com.chinatelecom.xysq.service.ForumService;
import com.chinatelecom.xysq.service.IndexService;
import com.chinatelecom.xysq.service.UserService;

@Controller
public class ClientController {

	private final static Log log = LogFactory.getLog(ClientController.class);

	private SpringProperty springProperty;

	private AreaService areaService;

	private IndexService indexService;

	private UserService userService;
	
	private ForumService forumService;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setForumService(ForumService forumService) {
		this.forumService = forumService;
	}

	@RequestMapping("/image")
	public void image(@RequestParam(value = "imagePath") String imagePath,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (StringUtils.isNotEmpty(imagePath)) {
			File file = (new File(springProperty.getImageUrlPrefix() + "/"
					+ imagePath));
			byte[] data = FileUtils.readFileToByteArray(file);
			resp.setHeader("Content-Disposition", "attachment; filename="
					+ file.getName());
			if (imagePath.toLowerCase().endsWith("jpeg")) {
				resp.setContentType("image/jpeg");
			} else if (imagePath.toLowerCase().endsWith("png")) {
				resp.setContentType("image/png");
			} else if (imagePath.toLowerCase().endsWith("gif")) {
				resp.setContentType("image/gif");
			} else if (imagePath.toLowerCase().endsWith("jpg")) {
				resp.setContentType("image/jpg");
			}
			resp.getOutputStream().write(data);
			resp.getOutputStream().flush();
		}
	}

	@RequestMapping("/queryCommunityByArea")
	public void queryCommunityByArea(
			@RequestParam(value = "areaId") Long areaId,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String result = this.areaService.queryCommunityByArea(areaId);
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/queryCityAreas")
	public void queryCityAreas(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String result = this.areaService.queryCityAreas();
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/queryIndex")
	public void queryIndex(
			@RequestParam(value = "communityId", required = false) Long communityId,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String result = this.indexService.queryIndex(communityId);
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/queryAnnouncementAndNotice")
	public void queryAnnouncementAndNotice(
			@RequestParam(value = "communityId", required = false) Long communityId,
			@RequestParam(value = "announcement") boolean announcement,
			@RequestParam(value = "noticeCategory", required = false) String noticeCategory,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String result = this.indexService.queryAnnouncementAndNotice(
				communityId, announcement,
				NoticeCategory.convert(noticeCategory), page, pageSize);
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/queryBroadbandRemind")
	public void queryBroadbandRemind(
			@RequestParam(value = "msisdn", required = true) String msisdn,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String result = this.indexService.queryBroadbandRemind(msisdn);
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/login")
	public void login(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			LoginJson result = userService.login(userName, password);

			if (result.isSuccess()) {
				req.getSession().setAttribute("USER_ID",
						result.getUser().getId());
				result.getUser().setJsessionid(req.getSession().getId());
			}
			resp.setContentType("application/json");
			resp.getOutputStream().write(
					JSONObject.fromObject(result).toString().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/register")
	public void register(@RequestParam(value = "nickName") String nickName,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "verifyCode") String verifyCode,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			RegisterJson result = userService.register(nickName, password,
					msisdn, verifyCode);
			if (result.isSuccess()) {
				req.getSession().setAttribute("USER_ID",
						result.getUser().getId());
				result.getUser().setJsessionid(req.getSession().getId());
			}
			resp.setContentType("application/json");
			resp.getOutputStream().write(
					JSONObject.fromObject(result).toString().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	@RequestMapping("/queryTopic")
	public void queryTopic(@RequestParam(value = "communityId", required = true) Long communityId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String result = this.forumService.queryTopic(communityId,page, pageSize);
			try {
				resp.setContentType("application/json");
				resp.getOutputStream().write(result.getBytes("utf-8"));
			} catch (Exception e) {
				log.error("", e);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/saveTopic")
	public void saveTopic(
			@RequestParam(value = "topicId", required = false) Long topicId,
			@RequestParam(value = "title") String title,
			@RequestParam(value = "content") String content,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}

	@RequestMapping("/savePost")
	public void savePost(@RequestParam(value = "topicId") Long topicId,
			@RequestParam(value = "postId") Long postId,
			@RequestParam(value = "content") String content,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {

		} catch (Exception e) {
			log.error("", e);
		}
	}

}
