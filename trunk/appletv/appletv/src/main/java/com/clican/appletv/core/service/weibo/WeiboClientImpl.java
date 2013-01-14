package com.clican.appletv.core.service.weibo;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weibo4j.Account;
import weibo4j.model.Status;
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
		content.append("<table><tr><td valign=\"top\">");
		// append profile image
		content.append("<img width=\"50\" height=\"50\"src=\"");
		content.append(status.getUser().getProfileImageUrl());
		content.append("\" /></td>");
		// append user name
		content.append("<td>");
		content.append(status.getUser().getScreenName());
		// append content
		content.append("<p style=\"width: 800px\">");
		content.append(status.getText());
		content.append("</p>");

		if (status.getRetweetedStatus() != null) {
			// append refer user name
			content.append("<p>");
			content.append("@"
					+ status.getRetweetedStatus().getUser().getScreenName());
			content.append("</p>");

			// append refer content
			content.append("<p style=\"width: 800px\">");
			content.append(status.getRetweetedStatus().getText());
			content.append("</p> ");

			if (StringUtils.isNotEmpty(status.getRetweetedStatus()
					.getThumbnailPic())) {
				content.append("<img src=\"");
				content.append(status.getRetweetedStatus().getThumbnailPic());
				content.append("\" />");
			}
		} else {
			if (StringUtils.isNotEmpty(status.getThumbnailPic())) {
				content.append("<img src=\"");
				content.append(status.getThumbnailPic());
				content.append("\" />");
			}

		}
		content.append("</td></tr></table>");
		String imageFileName = this.generateImageFileName();
		String filePath = springProperty.getWeiboTempImageFolder()
				+ imageFileName;
		String urlPath = springProperty.getWeiboTempImageURL() + imageFileName;
		for (int i = 0; i < 10; i++) {
			HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
			imageGenerator.loadHtml(content.toString());
			imageGenerator.saveAsImage(filePath);
		}
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
}
