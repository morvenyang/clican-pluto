package com.chinatelecom.xysq.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.service.AreaService;
import com.chinatelecom.xysq.service.IndexService;

@Controller
public class ClientController {

	private final static Log log = LogFactory.getLog(ClientController.class);

	private SpringProperty springProperty;

	private AreaService areaService;

	private IndexService indexService;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
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
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
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
}
