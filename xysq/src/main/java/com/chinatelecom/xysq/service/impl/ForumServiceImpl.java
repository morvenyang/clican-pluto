package com.chinatelecom.xysq.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.dao.ForumDao;
import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.json.ForumPostJson;
import com.chinatelecom.xysq.json.ForumTopicJson;
import com.chinatelecom.xysq.json.UserJson;
import com.chinatelecom.xysq.model.ForumPost;
import com.chinatelecom.xysq.model.ForumTopic;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.ForumService;
import com.chinatelecom.xysq.util.DateJsonValueProcessor;
import com.chinatelecom.xysq.util.StringUtils;

public class ForumServiceImpl implements ForumService {

	private final static Log log = LogFactory.getLog(ForumServiceImpl.class);

	private SpringProperty springProperty;

	private ForumDao forumDao;

	private UserDao userDao;

	public void setForumDao(ForumDao forumDao) {
		this.forumDao = forumDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public String queryTopic(Long communityId, int page, int pageSize) {
		List<ForumTopic> forumList = forumDao.queryTopic(communityId, page,
				pageSize);
		List<ForumTopicJson> forumJsonList = new ArrayList<ForumTopicJson>();
		for (ForumTopic forumTopic : forumList) {
			ForumTopicJson forumJson = new ForumTopicJson();
			forumJson.setImages(new ArrayList<String>());
			forumJson.setId(forumTopic.getId());
			forumJson.setContent(forumTopic.getContent());
			forumJson.setCreateTime(forumTopic.getCreateTime());
			forumJson.setModifyTime(forumTopic.getModifyTime());
			forumJson.setPostNum(forumTopic.getPostNum());
			UserJson userJson = new UserJson();
			userJson.setId(forumTopic.getSubmitter().getId());
			userJson.setNickName(forumTopic.getSubmitter().getNickName());
			userJson.setMsisdn(forumTopic.getSubmitter().getMsisdn());
			forumJson.setSubmitter(userJson);
			forumJson.setTitle(forumTopic.getTitle());
			forumJsonList.add(forumJson);
			for (Image image : forumTopic.getImages()) {
				forumJson.getImages().add(
						springProperty.getServerUrl()
								+ springProperty.getContextPath()
								+ "/image.do?imagePath=" + image.getPath());
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		return JSONArray.fromObject(forumJsonList, jsonConfig).toString();
	}

	@Override
	public String queryPost(Long topicId, int page, int pageSize) {
		List<ForumPost> forumPostList = forumDao.queryPost(topicId, page,
				pageSize);
		List<ForumPostJson> forumPostJsonList = new ArrayList<ForumPostJson>();
		for (ForumPost forumPost : forumPostList) {
			ForumPostJson forumPostJson = new ForumPostJson();
			forumPostJson.setImages(new ArrayList<String>());
			forumPostJson.setId(forumPost.getId());
			forumPostJson.setCreateTime(forumPost.getCreateTime());
			forumPostJson.setModifyTime(forumPost.getModifyTime());
			UserJson userJson = new UserJson();
			userJson.setId(forumPost.getSubmitter().getId());
			userJson.setNickName(forumPost.getSubmitter().getNickName());
			userJson.setMsisdn(forumPost.getSubmitter().getMsisdn());
			forumPostJson.setSubmitter(userJson);
			forumPostJsonList.add(forumPostJson);
			for (Image image : forumPost.getImages()) {
				forumPostJson.getImages().add(
						springProperty.getServerUrl()
								+ springProperty.getContextPath()
								+ "/image.do?imagePath=" + image.getPath());
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		return JSONArray.fromObject(forumPostJsonList, jsonConfig).toString();
	}

	@Override
	public Image getImage(byte[] data, String fileName) {
		String suffix = org.apache.commons.lang.StringUtils.substringAfterLast(
				fileName, ".");
		String filePath = StringUtils.generateFilePathByDate()
				+ UUID.randomUUID().toString() + "." + suffix;
		try {
			FileUtils.writeByteArrayToFile(
					new File(springProperty.getImageUrlPrefix() + "/"
							+ filePath), data);
		} catch (Exception e) {
			log.error("", e);
		}
		Image image = new Image();
		image.setPath(filePath);
		image.setName(fileName);
		return image;
	}

	@Override
	public void saveTopic(Long submitterId, Long communityId, Long topicId,
			String title, String content, List<Image> images) {
		ForumTopic forumTopic = null;
		if (topicId != null) {
			forumTopic = this.forumDao.findTopicById(topicId);
			this.forumDao.deleteImagesForTopic(topicId);
		} else {
			forumTopic = new ForumTopic();
			forumTopic.setCreateTime(new Date());
			User submitter = userDao.findUserById(submitterId);
			forumTopic.setSubmitter(submitter);
			forumTopic.setCommunityId(communityId);
		}
		forumTopic.setTitle(title);
		forumTopic.setContent(content);
		forumTopic.setModifyTime(new Date());
		this.forumDao.saveForumTopic(forumTopic);
		for (Image image : images) {
			image.setForumTopic(forumTopic);
			this.forumDao.saveImage(image);
		}
	}

}
