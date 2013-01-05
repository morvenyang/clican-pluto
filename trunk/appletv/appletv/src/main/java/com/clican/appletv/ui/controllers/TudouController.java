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
import com.clican.appletv.core.service.tudou.TudouClient;
import com.clican.appletv.core.service.tudou.enumeration.Channel;
import com.clican.appletv.core.service.tudou.model.TudouVideo;

@Controller
public class TudouController {

	private final static Log log = LogFactory.getLog(TudouController.class);

	@Autowired
	private TudouClient tudouClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/play.xml")
	public void planVideo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("itemid") String itemid)
			throws IOException {
		String playXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"com.sample.video-player\"><httpFileVideoAsset id=\""
				+ itemid
				+ "\"><mediaURL>http://vr.tudou.com/v2proxy/v2.m3u8?st=2&amp;it="
				+ itemid
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

	@RequestMapping("/tudou/index.xml")
	public String indexPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false) Integer page)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("access tudou index page");
		}
		if (page == null) {
			page = 0;
		}
		List<TudouVideo> videos = tudouClient.queryVideos(springProperty
				.getTudouRecommendApi() + "&page=" + page);
		request.setAttribute("channels", Channel.values());
		request.setAttribute("videos", videos);
		request.setAttribute("channelCount", Channel.values().length + 1);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("pagiurl", springProperty.getSystemServerUrl()
				+ "/tudou/index.xml?1=1");
		int begin, end = 0;
		if (page < 90) {
			begin = page;
			end = page + 9;
		} else {
			end = 99;
			begin = 90;
		}
		request.setAttribute("begin",begin);
		request.setAttribute("end",end);
		return "tudou/index";
	}

	@RequestMapping("/tudou/channel.xml")
	public String channelPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("channelId") int channelId,
			@RequestParam(value = "page", required = false) Integer page)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("access tudou channel page");
		}
		if (page == null) {
			page = 0;
		}
		List<TudouVideo> videos = tudouClient.queryVideos(springProperty
				.getTudouChannelApi()
				+ "&columnid="
				+ channelId
				+ "&page="
				+ page);
		request.setAttribute("channels", Channel.values());
		request.setAttribute("videos", videos);
		request.setAttribute("channelCount", Channel.values().length + 1);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("pagiurl", springProperty.getSystemServerUrl()
				+ "/tudou/channel.xml?channelId="+channelId);
		int begin, end = 0;
		if (page < 90) {
			begin = page;
			end = page + 9;
		} else {
			end = 99;
			begin = 90;
		}
		request.setAttribute("begin",begin);
		request.setAttribute("end",end);
		return "tudou/index";
	}
}
