package com.clican.appletv.core.service.lbl;

import java.util.concurrent.ConcurrentHashMap;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

import com.clican.appletv.core.service.BaseClient;

public class LblClientImpl extends BaseClient implements LblClient {

	private ConcurrentHashMap<String, String> imageMap = new ConcurrentHashMap<String, String>();

	@Override
	public String getImageUrl(String url) {
		if (imageMap.containsKey(url)) {
			return imageMap.get(url);
		} else {
			String imgUrl = null;
			String htmlContent = this.httpGet(url);
			Parser parser = Parser.createParser(htmlContent, "utf-8");
			try {
				AndFilter entryFilter = new AndFilter(new TagNameFilter("div"),
						new HasAttributeFilter("class", "entry"));
				NodeList entryNodeList = parser.parse(entryFilter);
				if (entryNodeList.size() > 0) {
					TagNode entry = (TagNode) entryNodeList.elementAt(0);
					for (int i = 0; i < entry.getChildren().size(); i++) {
						Node node = entry.getChildren().elementAt(i);
						if (node instanceof TagNode) {
							TagNode pNode = (TagNode) node;
							if (pNode.getTagName().equalsIgnoreCase("p")) {
								for (int j = 0; j < pNode.getChildren().size(); j++) {
									Node imageNode = pNode.getChildren()
											.elementAt(j);
									if (imageNode instanceof TagNode) {
										TagNode imageTagNode = (TagNode) imageNode;
										if (imageTagNode.getTagName()
												.equalsIgnoreCase("img")) {
											imgUrl = imageTagNode
													.getAttribute("src");
											break;
										}
									}
								}
							}
						}
						if (imgUrl != null) {
							break;
						}
					}
				}
				imageMap.put(url, imgUrl);
				return imgUrl;
			} catch (Exception e) {
				log.error("", e);
				return null;
			}
		}
	}

}
