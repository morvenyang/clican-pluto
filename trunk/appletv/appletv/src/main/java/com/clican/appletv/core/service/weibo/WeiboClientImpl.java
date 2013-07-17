package com.clican.appletv.core.service.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
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

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}



	@Override
	public void processLongUrl(StatusWapper statusWapper, String accessToken) {
		ShortUrl shortUrl = new ShortUrl();
		shortUrl.client.setToken(accessToken);
		List<String> urls = new ArrayList<String>();
		Map<String, List<Status>> urlMap = new HashMap<String, List<Status>>();
		Pattern pattern = Pattern.compile(
				springProperty.getWeiboShortURLPattern(), Pattern.DOTALL);
		Pattern youkuPattern = Pattern.compile(springProperty
				.getYoukuShowidPattern());
		Pattern qqPattern = Pattern.compile(springProperty.getQqIdPattern());
		Pattern tudouPattern = Pattern.compile(springProperty
				.getTudouCodePattern());
		Pattern sohuPattern = Pattern.compile(springProperty
				.getSohuURLPattern());
		Pattern fiveSixPattern = Pattern.compile(springProperty
				.getFivesixCodePattern());

		Pattern sinaMusicPattern = Pattern.compile(springProperty
				.getSinaMusicIdPattern());
		Pattern xiamiMusicPattern = Pattern.compile(springProperty
				.getXiamiMusicIdPattern());
		Pattern applePodcastPattern = Pattern.compile(springProperty
				.getApplePodcastUrlPattern());
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
					List<Status> list = urlMap.get(matchText);
					if (list == null) {
						list = new ArrayList<Status>();
						urlMap.put(matchText, list);
					}
					list.add(status);
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
						List<Status> list = urlMap.get(surl);
						if (type.equals("1")) {
							// video
							// convert this lurl to AppleTV page
							Matcher youkuMatcher = youkuPattern.matcher(lurl);
							if (youkuMatcher.matches()) {
								String showid = youkuMatcher.group(1);
								if(StringUtils.isEmpty(showid)){
									showid = youkuMatcher.group(2);
								}
								addVideoUrlForStatus(list, "atv.loadURL('"
										+ springProperty.getSystemServerUrl()
										+ "/ctl/youku/album.xml?showid="
										+ showid + "');");
							} else {
								Matcher qqMatcher = qqPattern.matcher(lurl);
								if (qqMatcher.matches()) {
									String coverId = qqMatcher.group(1);
									if (lurl.contains("cover")) {
										addVideoUrlForStatus(list,
												"qqClient.loadAlbumPage('"
														+ coverId + "');");
									} else {
										addVideoUrlForStatus(list,
												"qqClient.playVideo('"
														+ coverId + "');");
									}

								} else {
									Matcher tudouMatcher = null;
									tudouMatcher = tudouPattern.matcher(lurl);
									if (tudouMatcher.matches()) {
										String code = tudouMatcher.group(1);
										addVideoUrlForStatus(
												list,
												"atv.loadURL('"
														+ springProperty
																.getSystemServerUrl()
														+ "/ctl/tudou/playVideoByCode.xml?code="
														+ code + "');");
									} else {
										Matcher sohuMatcher = sohuPattern
												.matcher(lurl);
										if (sohuMatcher.matches()) {
											addVideoUrlForStatus(
													list,
													"atv.loadURL('"
															+ springProperty
																	.getSystemServerUrl()
															+ "/ctl/sohu/playVideoByURL.xml?url="
															+ lurl + "');");
										} else {
											Matcher fiveSixMatcher = fiveSixPattern
													.matcher(lurl);
											if (fiveSixMatcher.matches()) {
												String code = fiveSixMatcher
														.group(2);
												addVideoUrlForStatus(
														list,
														"atv.loadURL('"
																+ springProperty
																		.getSystemServerUrl()
																+ "/ctl/fivesix/playVideoByCode.xml?code="
																+ code + "');");
											}
										}
									}
								}
							}
							addUnknownUrlForStatus(list, lurl);
						} else if (type.equals("2")) {
							// music
							Matcher sinaMusicMatcher = sinaMusicPattern
									.matcher(lurl);
							if (sinaMusicMatcher.matches()) {
								String id = sinaMusicMatcher.group(2);
								addMusicUrlForStatus(list,
										springProperty.getSystemServerUrl()
												+ "/ctl/sina/music.xml?id="
												+ id);
							} else {
								Matcher xiamiMusicMatcher = xiamiMusicPattern
										.matcher(lurl);
								if (xiamiMusicMatcher.matches()) {
									String id = xiamiMusicMatcher.group(1);
									addMusicUrlForStatus(
											list,
											springProperty.getSystemServerUrl()
													+ "/ctl/xiami/music.xml?id="
													+ id);
								}
							}
							addUnknownUrlForStatus(list, lurl);
						} else if (type.equals("0")) {
							Matcher applePodcastMatcher = applePodcastPattern
									.matcher(lurl);
							if (applePodcastMatcher.matches()) {
								addPodcastUrlForStatus(list, lurl);
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

	private void addVideoUrlForStatus(List<Status> list, String videoUrl) {
		for (Status status : list) {
			status.setVideoUrl(videoUrl);
		}
	}

	private void addMusicUrlForStatus(List<Status> list, String videoUrl) {
		for (Status status : list) {
			status.setMusicUrl(videoUrl);
		}
	}

	private void addPodcastUrlForStatus(List<Status> list, String podcastUrl) {
		for (Status status : list) {
			status.setPodcastUrl(podcastUrl);
		}
	}

	private void addUnknownUrlForStatus(List<Status> list, String videoUrl) {
		for (Status status : list) {
			status.setUnknownUrl(videoUrl);
		}
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
		if(StringUtils.isEmpty(deviceId)){
			return null;
		}
		String accessToken = deviceIdAccessTokenMap.get(deviceId);
		return accessToken;
	}

	@Override
	public String getUid(String deviceId) {
		if(StringUtils.isEmpty(deviceId)){
			return null;
		}
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
		String s = "http://www.56.com/u59/v_NzA1NzAzMjg.html/1030_r412182261.html";
		String p = "http://www\\.56\\.com/u59/v_(\\p{Alnum}*)\\.html.*";
		Pattern pattern = Pattern.compile(p);
		Matcher youkuMatcher = pattern.matcher(s);
		if (youkuMatcher.matches()) {
			String showid = youkuMatcher.group(1);
			System.out.println(showid);
		}
	}
}
