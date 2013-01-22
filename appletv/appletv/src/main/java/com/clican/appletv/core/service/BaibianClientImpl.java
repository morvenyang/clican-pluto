package com.clican.appletv.core.service;

import java.util.ArrayList;
import java.util.List;

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
		String url = springProperty.getBaibianChannelApi() + (page * 30);

		if (log.isDebugEnabled()) {
			log.debug(url);
		}
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
