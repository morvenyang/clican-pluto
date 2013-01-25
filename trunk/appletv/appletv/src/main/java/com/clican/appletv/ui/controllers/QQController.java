package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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

import com.clican.appletv.common.Keyword;
import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.qq.QQClient;
import com.clican.appletv.core.service.qq.enumeration.Channel;
import com.clican.appletv.core.service.qq.model.QQAlbum;
import com.clican.appletv.core.service.tudou.TudouClient;

@Controller
public class QQController {

	private final static Log log = LogFactory.getLog(QQController.class);

	@Autowired
	private QQClient qqClient;
	
	@Autowired
	private TudouClient tudouClient;

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/qq/index.xml")
	public String indexPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "channelId", required = false) Integer channelId)
			throws IOException {

		Date startDate = new Date();
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

		List<Object> videos = qqClient.queryVideos(keyword, channel, page);
		if (videos.size() == 0) {
			return "qq/noresult";
		}
		request.setAttribute("channels", Channel.values());
		request.setAttribute("videos", videos);
		request.setAttribute("playdescurl", springProperty.getQqVideoPlayApi()
				.replaceAll("&", "&amp;"));
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		String pagiurl = springProperty.getSystemServerUrl()
				+ "/qq/index.xml?channelId=" + channel.getValue();
		if (StringUtils.isNotEmpty(keyword) && channel == Channel.Search) {
			pagiurl = pagiurl + "&amp;keyword="
					+ URLEncoder.encode(keyword, "utf-8");
		}
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

		Date endDate = new Date();
		if (log.isDebugEnabled()) {
			log.debug("spend " + (endDate.getTime() - startDate.getTime())
					+ " ms");
		}
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
		request.setAttribute("playdescurl", springProperty.getQqVideoPlayApi()
				.replaceAll("&", "&amp;"));
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "qq/album";
	}

	@RequestMapping("/qq/albumlist.xml")
	public String albumListPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access qq album page");
		}
		QQAlbum album = (QQAlbum) request.getSession().getAttribute("album");
		request.setAttribute("album", album);
		request.setAttribute("playdescurl", springProperty.getQqVideoPlayApi()
				.replaceAll("&", "&amp;"));
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "qq/albumlist";
	}

	@RequestMapping("/qq/search.xml")
	public String searchPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("access search page");
		}
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "qq/search";
	}

	@RequestMapping("/qq/keywrodsearchlist.xml")
	public String keywordSearchListPage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "q", required = false) String q)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("query keywords:" + q);
		}
		
		List<String> keywordList = new ArrayList<String>();
		if(StringUtils.isNotEmpty(q)){
			keywordList=tudouClient.queryKeywords(q);
		}
		
		List<Keyword> klist = new ArrayList<Keyword>();
		for (String kw : keywordList) {
			Keyword keyword = new Keyword();
			keyword.setLabel(kw);
			keyword.setUrlValue(URLEncoder.encode(kw, "UTF-8"));
			klist.add(keyword);
		}
		request.setAttribute("keywordList", klist);
		request.setAttribute("q", q);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "qq/keywrodsearchlist";
	}
}
