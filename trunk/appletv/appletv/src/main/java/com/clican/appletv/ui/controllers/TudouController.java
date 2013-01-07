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
import com.clican.appletv.core.service.tudou.model.ListView;
import com.clican.appletv.core.service.tudou.model.TudouAlbum;

@Controller
public class TudouController {

	private final static Log log = LogFactory.getLog(TudouController.class);

	@Autowired
	private TudouClient tudouClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/tudou/releasenote.xml")
	public String indexPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "tudou/releasenote";
	}

	@RequestMapping("/tudou/play.xml")
	public void planVideo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("itemid") Long itemid,
			@RequestParam(value = "st", required = false) Integer st)
			throws IOException {
		if (st == null) {
			st = 2;
		}
		String playXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><atv><body><videoPlayer id=\"com.sample.video-player\"><httpFileVideoAsset id=\""
				+ itemid
				+ "\"><mediaURL>http://vr.tudou.com/v2proxy/v2.m3u8?st="
				+ st
				+ "&amp;it="
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
		List<ListView> videos = tudouClient.queryVideos(null, page);
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
		request.setAttribute("begin", begin);
		request.setAttribute("end", end);
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

		request.setAttribute("channels", Channel.values());

		request.setAttribute("channelCount", Channel.values().length + 1);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		request.setAttribute("pagiurl", springProperty.getSystemServerUrl()
				+ "/tudou/channel.xml?channelId=" + channelId);

		Channel channel = Channel.convertToChannel(channelId);
		List<ListView> videos = tudouClient.queryVideos(channel, page);
		request.setAttribute("videos", videos);
		request.setAttribute("isAlbum", channel.isAlbum());
		request.setAttribute("page", page);
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
		return "tudou/index";
	}

	@RequestMapping("/tudou/album.xml")
	public String albumPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "itemid", required = false) Long itemid,
			@RequestParam(value = "hd", required = false) Integer hd,
			@RequestParam(value = "channelId", required = false) Integer channelId,
			@RequestParam(value = "page", required = false) Integer page)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access album page itemid=" + itemid + " hd=" + hd
					+ " channelId=" + channelId);
		}
		Channel channel = Channel.convertToChannel(channelId);
		List<ListView> videos = tudouClient.queryVideos(channel, page);
		TudouAlbum album = null;
		for (ListView ta : videos) {
			if (ta.getItemid().equals(itemid)) {
				album = (TudouAlbum) ta;
			}
		}
		request.setAttribute("album", album);
		request.setAttribute("hd", hd);
		request.setAttribute("channelId", channelId);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "tudou/album";
	}

	@RequestMapping("/tudou/albumlist.xml")
	public String albumListPage(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "st") Integer st)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access album list page");
		}
		TudouAlbum album = (TudouAlbum) request.getSession().getAttribute(
				"album");
		request.setAttribute("album", album);
		request.setAttribute("st", st);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "tudou/albumlist";
	}

	@RequestMapping("/tudou/search.xml")
	public String keywordSearchListPage(HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access search page");
		}
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "tudou/search";
	}
	
	@RequestMapping("/tudou/keywrodsearchlist.xml")
	public String keywordSearchListPage(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "q") String q)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("query keywords");
		}
		List<String> keywordList = tudouClient.queryKeywords(q);
		request.setAttribute("keywordList", keywordList);
		request.setAttribute("q", q);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "tudou/keywrodsearchlist";
	}

}
