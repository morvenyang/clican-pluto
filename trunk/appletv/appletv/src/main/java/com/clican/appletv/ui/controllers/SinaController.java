package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.Music;
import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.sina.SinaClient;

@Controller
public class SinaController {

	private final static Log log = LogFactory.getLog(SinaController.class);

	@Autowired
	private SinaClient sinaClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/sina/music.xml")
	public String musicPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access sina music id=" + id);
		}
		Music sinaMusic = sinaClient.getMusic(id);
		if (StringUtils.isNotEmpty(sinaMusic.getMp3Url())) {
			sinaMusic.setMp3Url(URLEncoder.encode(sinaMusic.getMp3Url(),
					"utf-8"));
		}
		sinaMusic.setId(id);
		request.setAttribute("music", sinaMusic);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "sina/music";
	}

	@RequestMapping("/sina/playMusic.xml")
	public void playMusicPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "playUrlDesc", required = true) String playUrlDesc)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("play music playUrlDesc=" + playUrlDesc);
		}

		String mp3Url = sinaClient.getMp3Url(playUrlDesc);

		if (log.isDebugEnabled()) {
			log.debug("mp3 url:" + mp3Url);
		}

	}
}
