package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.io.OutputStream;

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
import com.clican.appletv.core.service.xiami.XiamiClient;

@Controller
public class XiaomiController {

	private final static Log log = LogFactory.getLog(XiaomiController.class);
	
	@Autowired
	private SpringProperty springProperty;
	
	@Autowired
	private XiamiClient xiamiClient;

	@RequestMapping("/xiami/music.xml")
	public String musicPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "podcastURL", required = true) String podcastURL)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("play music id=" + id);
		}

		Music music = xiamiClient.getMusic(id);
		request.setAttribute("music", music);
		if(StringUtils.isNotEmpty(podcastURL)){
			request.setAttribute("podcastURL", podcastURL);
		}
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "xiami/music";
	}

	@RequestMapping("/xiami/playMusic.xml")
	public void playMusicPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "playUrl", required = true) String playUrl)
			throws IOException {
		playUrl=playUrl.replaceAll("&", "&amp;").replaceAll("\\s", "%20");
		
		if (log.isDebugEnabled()) {
			log.debug("mp3 url:" + playUrl);
		}
		String playXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"com.sample.video-player\"><httpLiveStreamingVideoAsset id=\""
				+ "hfva"
				+ "\"><mediaURL>"
				+ playUrl
				+ "</mediaURL><title></title><description></description></httpLiveStreamingVideoAsset></videoPlayer></body></atv>";
		byte[] data = playXml.getBytes("utf-8");
		OutputStream os = null;
		try {
			response.setContentType("text/xml");
			response.setContentLength(data.length);
			os = response.getOutputStream();
			os.write(data);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"System Error");
			return;
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
}
