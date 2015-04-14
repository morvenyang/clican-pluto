package com.chinatelecom.xysq.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.json.ProfileJson;
import com.chinatelecom.xysq.json.ResultJson;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.service.AreaService;
import com.chinatelecom.xysq.service.AwardService;
import com.chinatelecom.xysq.service.ForumService;
import com.chinatelecom.xysq.service.IndexService;
import com.chinatelecom.xysq.service.StoreService;
import com.chinatelecom.xysq.service.UserService;

@Controller
public class ClientController {

	private final static Log log = LogFactory.getLog(ClientController.class);

	private SpringProperty springProperty;

	private AreaService areaService;

	private IndexService indexService;

	private UserService userService;

	private ForumService forumService;

	private StoreService storeService;
	
	private AwardService awardService;

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

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}

	public void setAwardService(AwardService awardService) {
		this.awardService = awardService;
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
			ProfileJson result = userService.login(userName, password);

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
			ProfileJson result = userService.register(nickName, password,
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

	@RequestMapping("/forgetPassword")
	public void forgetPassword(
			@RequestParam(value = "password") String password,
			@RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "verifyCode") String verifyCode,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			ProfileJson result = userService.forgetPassword(password, msisdn,
					verifyCode);
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

	@RequestMapping("/updateProfile")
	public void updateProfile(
			@RequestParam(value = "nickName") String nickName,
			@RequestParam(value = "address") String address,
			@RequestParam(value = "carNumber") String carNumber,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Long userId = (Long) req.getSession().getAttribute("USER_ID");
			ProfileJson result = null;
			if (userId == null) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				result = new ProfileJson();
				result.setSuccess(false);
				result.setMessage("请先登录");
			} else {
				result = userService.updateProfile(userId, nickName, address,
						carNumber);
			}

			resp.setContentType("application/json");
			resp.getOutputStream().write(
					JSONObject.fromObject(result).toString().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/queryTopic")
	public void queryTopic(
			@RequestParam(value = "communityId", required = true) Long communityId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String result = this.forumService.queryTopic(communityId, page,
					pageSize);
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

	@RequestMapping("/queryPost")
	public void queryPost(
			@RequestParam(value = "topicId", required = true) Long topicId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String result = this.forumService
					.queryPost(topicId, page, pageSize);
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
	public void saveTopic(MultipartHttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		Long communityId = Long.parseLong(req.getParameter("communityId"));
		Long topicId = null;
		if (StringUtils.isNotEmpty(req.getParameter("topicId"))) {
			topicId = Long.parseLong(req.getParameter("topicId"));
		}
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		List<Image> images = new ArrayList<Image>();
		for (Object fileName : req.getFileMap().keySet()) {
			MultipartFile file = req.getFile((String) fileName);
			InputStream is = file.getInputStream();
			try {
				byte[] data = new byte[(int) file.getSize()];
				is.read(data);
				Image image = this.forumService.getImage(data,
						(String) fileName);
				images.add(image);
			} catch (Exception e) {
				log.error("", e);
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
		Long userId = (Long) req.getSession().getAttribute("USER_ID");
		this.forumService.saveTopic(userId, communityId, topicId, title,
				content, images);
	}

	@RequestMapping("/savePost")
	public void savePost(MultipartHttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		try {
			Long topicId = Long.parseLong(req.getParameter("topicId"));
			Long postId = null;
			if (StringUtils.isNotEmpty(req.getParameter("postId"))) {
				postId = Long.parseLong(req.getParameter("postId"));
			}
			String content = req.getParameter("content");
			String replyContent = req.getParameter("replyContent");
			List<Image> images = new ArrayList<Image>();
			for (Object fileName : req.getFileMap().keySet()) {
				MultipartFile file = req.getFile((String) fileName);
				InputStream is = file.getInputStream();
				try {
					byte[] data = new byte[(int) file.getSize()];
					is.read(data);
					Image image = this.forumService.getImage(data,
							(String) fileName);
					images.add(image);
				} catch (Exception e) {
					log.error("", e);
				} finally {
					if (is != null) {
						is.close();
					}
				}
			}
			Long userId = (Long) req.getSession().getAttribute("USER_ID");
			this.forumService.savePost(userId, topicId, postId, content,
					replyContent, images);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/queryStore")
	public void queryStore(
			@RequestParam(value = "storeId", required = true) Long storeId,

			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String result = this.storeService.queryStore(storeId);
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

	@RequestMapping("/applyXqnc")
	public void applyXqnc(
			@RequestParam(value = "carNumber", required = true) String carNumber,

			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			ResultJson rj = null;
			Long userId = (Long) req.getSession().getAttribute("USER_ID");
			if (userId == null) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				rj = new ResultJson(false, "请先登录");
			} else {
				this.userService.enableXqnc(userId, carNumber);
				rj = new ResultJson(true, "申请成功");
			}
			resp.setContentType("application/json");
			resp.getOutputStream().write(
					JSONObject.fromObject(rj).toString().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/cancelXqnc")
	public void cancelXqnc(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			ResultJson rj = null;
			Long userId = (Long) req.getSession().getAttribute("USER_ID");
			if (userId == null) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				rj = new ResultJson(false, "请先登录");
			} else {
				this.userService.disableXqnc(userId);
				rj = new ResultJson(true, "取消成功");
			}
			resp.setContentType("application/json");
			resp.getOutputStream().write(
					JSONObject.fromObject(rj).toString().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/lottery")
	public void lottery(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String result;
		Long userId = (Long) req.getSession().getAttribute("USER_ID");
		if (userId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ResultJson rj = new ResultJson(false, "请先登录");
			result = JSONObject.fromObject(rj).toString();
		} else {
			result = this.awardService.lottery(userId);
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
