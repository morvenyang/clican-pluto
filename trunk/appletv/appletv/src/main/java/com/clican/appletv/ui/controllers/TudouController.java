package com.clican.appletv.ui.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.clican.appletv.core.service.tudou.TudouClient;
import com.clican.appletv.core.service.tudou.enumeration.Area;
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

	@RequestMapping("/jstest.do")
	public String jstest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		log.debug(request.getSession().getAttribute("taobaoHtmlToken"));
		log.debug(request.getSession().getAttribute("taobaoHtmlTid"));
		return "jstest";
	}

	@RequestMapping("/tudou/releasenote.xml")
	public String indexPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "tudou/releasenote";
	}


	@RequestMapping("/tudou/playVideoByCode.xml")
	public void playVideoByCode(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("code") String code,
			@RequestParam(value = "st", required = false) Integer st)
			throws IOException {
		if (st == null) {
			st = 2;
		}
		Long itemid = tudouClient.getItemid(code);
		if (itemid != null) {
			playVideo(request, response, itemid, st);
		}
	}

	@RequestMapping("/tudou/play.xml")
	public void playVideo(HttpServletRequest request,
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
			@RequestParam(value = "channelId", required = false) Integer channelId,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "area", required = false) Integer area)
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

		if (year == null) {
			year = -1;
		}
		if (area == null) {
			area = -1;
		}
		List<ListView> videos = tudouClient.queryVideos(keyword, channel, year,
				area, page);
		if (videos.size() == 0) {
			return "tudou/noresult";
		}
		request.setAttribute("channels", Channel.values());
		request.setAttribute("channelId", channelId);
		request.setAttribute("year", year);
		request.setAttribute("area", area);
		request.setAttribute("videos", videos);
		request.setAttribute("channelCount", Channel.values().length + 1);
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		String pagiurl = springProperty.getSystemServerUrl()
				+ "/ctl/tudou/index.xml?channelId=" + channel.getValue();
		if (StringUtils.isNotEmpty(keyword) && channel == Channel.Search) {
			pagiurl = pagiurl + "&amp;keyword="
					+ URLEncoder.encode(keyword, "utf-8");

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
			HttpServletResponse response,
			@RequestParam(value = "q", required = false) String q)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("query keywords:" + q);
		}
		List<String> keywordList = tudouClient.queryKeywords(q);
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
		return "tudou/keywrodsearchlist";
	}

	@RequestMapping("/tudou/filter.xml")
	public String filterPage(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "channelId", required = false) Integer channelId,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "area", required = false) Integer area)
			throws IOException {
		if (year == null) {
			year = -1;
		}
		if (area == null) {
			area = -1;
		}
		request.setAttribute("channelId", channelId);
		request.setAttribute("selectedYear", year);
		request.setAttribute("selectedArea", area);
		request.setAttribute("areas", Area.values());
		request.setAttribute("currentYear",
				Calendar.getInstance().get(Calendar.YEAR));
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "tudou/filter";
	}

}
