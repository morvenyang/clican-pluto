package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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

	@RequestMapping("/tudou/log.do")
	public void indexPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "log", required = false) String logText)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug(logText);
		}
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
	public String indexPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "channelId", required = false) Integer channelId)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("access tudou index page");
		}
		if (page == null) {
			page = 0;
		}
		Channel channel = Channel.Recommand;

		if (channelId != null) {
			channel = Channel.convertToChannel(channelId);
		}

		List<ListView> videos = tudouClient.queryVideos(keyword, channel, page);
		request.setAttribute("channels", Channel.values());
		request.setAttribute("videos", videos);
		request.setAttribute("channelCount", Channel.values().length + 1);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		String pagiurl = springProperty.getSystemServerUrl()
				+ "/tudou/index.xml?channelId=" + channel.getValue();
		if (StringUtils.isNotEmpty(keyword) && channel == Channel.Search) {
			pagiurl = pagiurl + "&amp;keyword=" + keyword;

		}
		request.setAttribute("pagiurl", pagiurl);
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

	@RequestMapping("/tudou/test.xml")
	public void testPage() {
		if (log.isDebugEnabled()) {
			log.debug("access test page");
		}
	}

	@RequestMapping("/tudou/album.xml")
	public String albumPage(HttpServletRequest request,
			HttpServletResponse response, String data) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access album page data=" + data);
		}
		TudouAlbum album = tudouClient.queryAlbum(data);
		request.getSession().setAttribute("album", album);
		request.setAttribute("album", album);
		request.setAttribute("hd", album.getHd());
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
	public String searchPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
