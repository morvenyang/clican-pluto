package com.clican.appletv.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

import com.clican.appletv.core.model.Baibian;

public class BaibianClientImpl extends BaseClient implements BaibianClient {

	private List<Baibian> convertToVideos(String htmlContent) {
		List<Baibian> result = new ArrayList<Baibian>();
		Parser parser = Parser.createParser(htmlContent, "utf-8");
		AndFilter singleFilter = new AndFilter(new TagNameFilter("div"),
				new HasAttributeFilter("class", "mvListSingle"));
		try {
			NodeList nodeList = parser.parse(singleFilter);
			for (int i = 0; i < nodeList.size(); i++) {
				TagNode node = (TagNode) nodeList.elementAt(i);
				TagNode imageNode = (TagNode) node.getFirstChild();
				String imageUrl = imageNode.getAttribute("style");
				imageUrl = imageUrl.replace("background:url(", "").replace(")",
						"");
				TagNode mediaHtmlUrlNode = (TagNode) imageNode.getFirstChild()
						.getFirstChild();
				String mediaHtmlUrl = mediaHtmlUrlNode.getAttribute("href");
				TagNode titleNode = (TagNode) node.getFirstChild()
						.getFirstChild();
				String title = titleNode.getText();
				Baibian baibian = new Baibian();
				baibian.setTitle(title);
				baibian.setImageUrl(imageUrl);
				baibian.setMediaHtmlUrl(mediaHtmlUrl);
				result.add(baibian);
			}
		} catch (Exception e) {
			log.error("", e);
		}

		return result;
	}

	@Override
	public List<Baibian> queryVideos(int page) {
		this.checkCache();

		String htmlContent;
		String url = springProperty.getBaibianChannelApi() + (page * 15);

		if (log.isDebugEnabled()) {
			log.debug(url);
		}
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17");
		headers.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("Cookie", "saeut=15.219.153.81.1358831739353792; PHPSESSID=9f4a55d8eb16e118509be6dd0e5e9049; __utma=51367610.2078390930.1358831751.1358831751.1358837682.2; __utmb=51367610.2.10.1358837682; __utmc=51367610; __utmz=51367610.1358831751.1.1.utmcsr=apps.weibo.com|utmccn=(referral)|utmcmd=referral|utmcct=/dianying");
		//headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		//headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		
		if (cacheMap.containsKey(url)) {
			htmlContent = cacheMap.get(url);
		} else {
			synchronized (this) {
				if (cacheMap.containsKey(url)) {
					htmlContent = cacheMap.get(url);
				} else {
					htmlContent = httpGet(url, null, null);
					List<Baibian> result = convertToVideos(htmlContent);
					cacheMap.put(url, htmlContent);
					return result;
				}
			}
		}
		List<Baibian> result = convertToVideos(htmlContent);
		return result;
	}

}
