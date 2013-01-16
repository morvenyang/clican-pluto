package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.youku.YoukuClient;
import com.clican.appletv.core.service.youku.model.YoukuAlbum;

@Controller
public class YoukuController {

	private final static Log log = LogFactory.getLog(YoukuController.class);

	@Autowired
	private YoukuClient youkuClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/youku/play.xml")
	public void planVideo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("showid") String showid)
			throws IOException {
		String playXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"com.sample.video-player\"><httpFileVideoAsset id=\""
				+ "play"
				+ "\"><mediaURL>"
				+ springProperty.getYoukuVideoPlayApi().replace("showid",
						showid)
				+ "</mediaURL><title></title><description></description></httpFileVideoAsset></videoPlayer></body></atv>";
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

	@RequestMapping("/youku/album.xml")
	public String albumPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "showid", required = true) String showid)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access youku album page showid=" + showid);
		}
		YoukuAlbum album = youkuClient.queryAlbum(showid);
		request.getSession().setAttribute("album", album);
		request.setAttribute("showid", showid);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "youku/album";
	}
}
