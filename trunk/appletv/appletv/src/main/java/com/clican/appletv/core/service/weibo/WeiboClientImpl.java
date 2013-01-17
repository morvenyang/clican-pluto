package com.clican.appletv.core.service.weibo;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weibo4j.Account;
import weibo4j.ShortUrl;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

import com.clican.appletv.common.SpringProperty;

public class WeiboClientImpl implements WeiboClient {

	private final static Log log = LogFactory.getLog(WeiboClientImpl.class);

	private final static Map<String, String> deviceIdAccessTokenMap = new ConcurrentHashMap<String, String>();

	private SpringProperty springProperty;

	private final static AtomicLong imageFileName = new AtomicLong(Calendar
			.getInstance().getTimeInMillis());

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public void processLongUrl(StatusWapper statusWapper, String accessToken) {
		ShortUrl shortUrl = new ShortUrl();
		shortUrl.client.setToken(accessToken);
		List<String> urls = new ArrayList<String>();
		Map<String, Status> urlMap = new HashMap<String, Status>();
		Pattern pattern = Pattern.compile(springProperty
				.getWeiboShortURLPattern());
		Pattern youkuPattern = Pattern.compile(springProperty
				.getYoukuShowidPattern());
		Pattern qqPattern = Pattern.compile(springProperty.getQqIdPattern());
		Pattern tudouPattern = Pattern.compile(springProperty
				.getTudouCodePattern());
		Pattern sohuPattern = Pattern.compile(springProperty.getSohuURLPattern());
		for (Status status : statusWapper.getStatuses()) {
			try {
				String text = status.getText();
				if (status.getRetweetedStatus() != null
						&& StringUtils.isNotEmpty(status.getRetweetedStatus()
								.getText())) {
					text += status.getRetweetedStatus().getText();
				}
				Matcher matcher = pattern.matcher(text);
				while (matcher.find()) {
					String matchText = matcher.group();
					urls.add(matchText);
					urlMap.put(matchText, status);
				}
			} catch (Exception e) {
				log.error("", e);
			}

		}
		if (urls.size() > 0) {
			try {
				JSONArray jsonArray = shortUrl.shortToLongUrl(urls)
						.getJSONArray("urls");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonUrl = jsonArray.getJSONObject(i);
					String surl = jsonUrl.getString("url_short");
					String lurl = jsonUrl.getString("url_long");
					String type = jsonUrl.getString("type");
					try {
						if (type.equals("1")) {
							// video
							Status status = urlMap.get(surl);
							// convert this lurl to AppleTV page
							Matcher youkuMatcher = youkuPattern.matcher(lurl);
							if (youkuMatcher.matches()) {
								String showid = youkuMatcher.group(1);
								status.setVideoUrl(springProperty
										.getSystemServerUrl()
										+ "/youku/album.xml?showid=" + showid);
							} else {
								Matcher qqMatcher = qqPattern.matcher(lurl);
								if (qqMatcher.matches()) {
									String coverId = qqMatcher.group(1);
									status.setVideoUrl(springProperty
											.getSystemServerUrl()
											+ "/qq/album.xml?coverId="
											+ coverId);
								} else {
									Matcher tudouMatcher = tudouPattern
											.matcher(lurl);
									if (tudouMatcher.matches()) {
										String code = tudouMatcher.group(1);
										status.setVideoUrl(springProperty
												.getSystemServerUrl()
												+ "/tudou/playVideoByCode.xml?code="
												+ code);
									} else {
										Matcher sohuMatcher = sohuPattern.matcher(lurl);
										if (sohuMatcher.matches()) {
											status.setVideoUrl(springProperty
													.getSystemServerUrl()
													+ "/sohu/playVideoByURL.xml?url="
													+ lurl);
										}
									}
								}
							}

						}
					} catch (Exception e) {
						log.error("", e);
					}
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}

	}

	private String generateImageFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
		sdf.format(new Date());
		String fileName = sdf.format(new Date()) + "/"
				+ imageFileName.getAndIncrement() + ".png";
		return fileName;
	}

	public String generateWeiboImage(Status status) {
		Date start = new Date();
		StringBuffer content = new StringBuffer();
		content.append("<p style=\"width: 700px;font-size:60\">");
		content.append(status.getUser().getScreenName());
		content.append("</p>");
		// append content
		content.append("<p style=\"width: 700px;font-size:60\">");
		content.append(status.getText());
		content.append("</p>");

		if (status.getRetweetedStatus() != null
				&& status.getRetweetedStatus().getUser() != null) {
			// append refer user name
			content.append("<p style=\"width: 700px;font-size:60\">");
			content.append("@"
					+ status.getRetweetedStatus().getUser().getScreenName());
			content.append("</p>");

			// append refer content
			content.append("<p style=\"width: 700px;font-size:60\">");
			content.append(status.getRetweetedStatus().getText());
			content.append("</p> ");
		}
		String imageFileName = this.generateImageFileName();

		String filePath = springProperty.getWeiboTempImageFolder() + "/"
				+ imageFileName;
		String urlPath = springProperty.getWeiboTempImageURL() + "/"
				+ imageFileName;
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadHtml(content.toString());
		File weiboTempImageFile = new File(filePath);
		if (!weiboTempImageFile.getParentFile().exists()) {
			weiboTempImageFile.getParentFile().mkdirs();
		}
		imageGenerator.saveAsImage(filePath);
		Date end = new Date();
		if (log.isDebugEnabled()) {
			log.debug("spend " + (end.getTime() - start.getTime())
					+ " ms to generate png");
		}
		return urlPath;
	}

	@Override
	public boolean saveUserInfo(String accessToken, String deviceId) {
		Account account = new Account();
		account.client.setToken(accessToken);
		try {
			JSONObject uid = account.getUid();
			if (StringUtils.isNotEmpty(uid.getString("uid"))) {
				deviceIdAccessTokenMap.put(deviceId, accessToken);
				return true;
			} else {
				log.debug("Get the uid failure, the user must rebind once again.");
				return false;
			}
		} catch (Exception e) {
			log.debug("Get the uid failure, the user must rebind once again.");
		}
		return false;
	}

	public void init() {
		if (log.isInfoEnabled()) {
			log.info("Begin to load weibo token file");
		}
		InputStream is = null;
		try {
			File file = new File(springProperty.getWeiboTokenFile());
			if (!file.exists()) {
				return;
			}
			is = new FileInputStream(file);
			Properties props = new Properties();
			props.load(is);
			for (Entry<Object, Object> entry : props.entrySet()) {
				deviceIdAccessTokenMap.put((String) entry.getKey(),
						(String) entry.getValue());
			}
			if (log.isInfoEnabled()) {
				log.info("Load " + deviceIdAccessTokenMap.size()
						+ " tokens from weibo token file");
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
			log.info("Begin to persist weibo token file");
		}
		OutputStream os = null;
		try {
			File file = new File(springProperty.getWeiboTokenFile());
			if (file.exists()) {
				file.delete();
			}
			os = new FileOutputStream(springProperty.getWeiboTokenFile());
			for (String key : deviceIdAccessTokenMap.keySet()) {
				String entry = key + "=" + deviceIdAccessTokenMap.get(key)
						+ "\n";
				os.write(entry.getBytes("utf-8"));
			}
			if (log.isInfoEnabled()) {
				log.info("Persist " + deviceIdAccessTokenMap.size()
						+ " tokens into weibo token file");
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
	public String getAccessToken(String deviceId) {
		String accessToken = deviceIdAccessTokenMap.get(deviceId);
		return accessToken;
	}

	@Override
	public String getUid(String deviceId) {
		String accessToken = deviceIdAccessTokenMap.get(deviceId);
		if (StringUtils.isEmpty(accessToken)) {
			return null;
		}
		Account account = new Account();
		account.client.setToken(accessToken);
		try {
			String uid = account.getUid().getString("uid");
			if (StringUtils.isNotEmpty(uid)) {
				return uid;
			} else {
				log.debug("Get the uid failure, the user must rebind once again.");
				return null;
			}
		} catch (Exception e) {
			log.debug("Get the uid failure, the user must rebind once again.");
		}

		return null;
	}

	public static void main(String[] args) {
		String s = "http://v.youku.com/v_show/id_XNTAwMzg1OTEy.html";
		String p = "http://v.youku.com/v_show/id_(\\p{Alnum}*)\\.html";
		Pattern pattern = Pattern.compile(p);
		Matcher youkuMatcher = pattern.matcher(s);
		if (youkuMatcher.matches()) {
			String showid = youkuMatcher.group(1);
			System.out.println(showid);
		}
	}
}
