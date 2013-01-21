package com.clican.appletv.ui.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.Music;
import com.clican.appletv.core.service.xiami.XiamiClient;

@Controller
public class XiaomiController {

	private final static Log log = LogFactory.getLog(XiaomiController.class);

	@Autowired
	private XiamiClient xiamiClient;

	@RequestMapping("/xiami/music.xml")
	public String musicPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("play music id=" + id);
		}

		Music music = xiamiClient.getMusic(id);
		request.setAttribute("music", music);
		return "xiami/music.jsp";
	}

	@RequestMapping("/xiami/playMusic.xml")
	public void playMusicPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "playUrl", required = true) String playUrl)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("play music playUrl=" + playUrl);
		}

		if (log.isDebugEnabled()) {
			log.debug("mp3 url:" + playUrl);
		}

	}
}
