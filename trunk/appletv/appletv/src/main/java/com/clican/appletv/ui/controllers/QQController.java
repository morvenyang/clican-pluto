package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.qq.QQClient;
import com.clican.appletv.core.service.qq.enumeration.Channel;
import com.clican.appletv.core.service.qq.model.QQAlbum;
import com.clican.appletv.core.service.qq.model.QQVideo;
import com.clican.appletv.core.service.tudou.model.TudouAlbum;

@Controller
public class QQController {

	private final static Log log = LogFactory.getLog(QQController.class);

	@Autowired
	private QQClient qqClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/qq/play.xml")
	public void planVideo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("coverId") String coverId) throws IOException {
		String playXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"com.sample.video-player\"><httpFileVideoAsset id=\""
				+ coverId
				+ "\"><mediaURL>"
				+ springProperty.getQqVideoPlayApi()
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

	@RequestMapping("/qq/index.xml")
	public String indexPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "channelId", required = false) Integer channelId)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("access qq index page");
		}
		if (page == null) {
			page = 0;
		}
		Channel channel = Channel.Recommand;

		if (channelId != null) {
			channel = Channel.convertToChannel(channelId);
		}

		List<QQVideo> videos = qqClient.queryVideos(keyword, channel, page);
		if (videos.size() == 0) {
			return "qq/noresult";
		}
		request.setAttribute("channels", Channel.values());
		request.setAttribute("videos", videos);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		String pagiurl = springProperty.getSystemServerUrl()
				+ "/tudou/index.xml?channelId=" + channel.getValue();

		request.setAttribute("pagiurl", pagiurl);
		request.setAttribute("page", page);
		request.setAttribute("channel", channel);
		int begin, end = 0;
		if (page < 90) {
			begin = page;
			end = page + 9;
		} else {
			end = 99;
			begin = 90;
		}
		request.setAttribute("begin", begin);
		request.setAttribute("end", end);
		return "qq/index";
	}

	@RequestMapping("/qq/album.xml")
	public String albumPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "coverId", required = true) String coverId)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access qq album page coverId=" + coverId);
		}
		QQAlbum album = qqClient.queryAlbum(coverId);
		request.getSession().setAttribute("album", album);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "qq/album";
	}

	@RequestMapping("/11/albumlist.xml")
	public String albumListPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access qq album page");
		}
		TudouAlbum album = (TudouAlbum) request.getSession().getAttribute(
				"album");
		request.setAttribute("album", album);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "11/albumlist";
	}
}
