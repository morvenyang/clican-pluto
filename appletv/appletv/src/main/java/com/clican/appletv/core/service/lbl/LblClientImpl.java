package com.clican.appletv.core.service.lbl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

import com.clican.appletv.core.service.BaseClient;
import com.taobao.api.internal.util.StringUtils;

public class LblClientImpl extends BaseClient implements LblClient {

	private ConcurrentHashMap<String, String> imageMap = new ConcurrentHashMap<String, String>();

	public void init() {
		if (log.isInfoEnabled()) {
			log.info("Begin to lbl image map file");
		}
		InputStream is = null;
		try {
			File file = new File(springProperty.getLblImageMapFile());
			if (!file.exists()) {
				return;
			}
			is = new FileInputStream(file);
			Properties props = new Properties();
			props.load(is);
			for (Entry<Object, Object> entry : props.entrySet()) {
				imageMap.put((String) entry.getKey(), (String) entry.getValue());
			}
			if (log.isInfoEnabled()) {
				log.info("Load " + imageMap.size()
						+ " images mapping from lbl image map file");
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	public void destroy() {
		if (log.isInfoEnabled()) {
			log.info("Begin to persist lbl image map file");
		}
		OutputStream os = null;
		try {
			File file = new File(springProperty.getLblImageMapFile());
			if (file.exists()) {
				file.delete();
			}
			os = new FileOutputStream(springProperty.getLblImageMapFile());
			for (String key : imageMap.keySet()) {
				String entry = key + "=" + imageMap.get(key) + "\n";
				os.write(entry.getBytes("utf-8"));
			}
			if (log.isInfoEnabled()) {
				log.info("Persist " + imageMap.size()
						+ " lbl image map into file");
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

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
							if (pNode.getTagName()!=null&&pNode.getTagName().equalsIgnoreCase("p")) {
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
				if (StringUtils.isEmpty(imgUrl)) {
					imageMap.put(url, "");
				}else{
					imageMap.put(url, imgUrl);
				}
				return imgUrl;
			} catch (Exception e) {
				log.error("", e);
				imageMap.put(url, "");
				return null;
			}
		}
	}

}
