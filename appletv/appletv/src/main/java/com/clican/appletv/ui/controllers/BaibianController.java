package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.baibian.BaibianClient;
import com.clican.appletv.core.service.baibian.model.Baibian;

@Controller
public class BaibianController {

	@Autowired
	private SpringProperty springProperty;

	@Autowired
	private BaibianClient baibianClient;

	@RequestMapping("/baibian/index.xml")
	public String indexPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false) Integer page)
			throws IOException {

		if (page == null) {
			page = 1;
		}
		List<Baibian> videos = baibianClient.queryVideos(page);
		String pagiurl = springProperty.getSystemServerUrl()
				+ "/ctl/baibian/index.xml?1=1";
		request.setAttribute("pagiurl", pagiurl);
		request.setAttribute("page", page);
		int begin=1, end = 0;
		if (page < 90) {
			begin = page;
			end = page + 9;
		} else {
			end = 99;
			begin = 90;
		}
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("videos", videos);
		request.setAttribute("begin", begin);
		request.setAttribute("end", end);
		return "baibian/index";
	}
	
	@RequestMapping("/baibian/video.xml")
	public String videoPage(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("title") String title,
			@RequestParam("imageUrl") String imageUrl,
			@RequestParam("mediaUrl") String mediaUrl,
			@RequestParam("id") Long id) throws IOException {

		Baibian baibian = new Baibian();
		baibian.setId(id);
		baibian.setMediaUrl(mediaUrl);
		baibian.setImageUrl(imageUrl);
		baibian.setTitle(title);
		request.setAttribute("video", baibian);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "baibian/video";
	}
	
}
