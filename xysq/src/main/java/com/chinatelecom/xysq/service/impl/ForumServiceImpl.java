package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.dao.ForumDao;
import com.chinatelecom.xysq.json.ForumTopicJson;
import com.chinatelecom.xysq.json.UserJson;
import com.chinatelecom.xysq.model.ForumTopic;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.service.ForumService;
import com.chinatelecom.xysq.util.DateJsonValueProcessor;

public class ForumServiceImpl implements ForumService {

	private SpringProperty springProperty;
	
	private ForumDao forumDao;

	public void setForumDao(ForumDao forumDao) {
		this.forumDao = forumDao;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public String queryTopic(int page, int pageSize) {
		List<ForumTopic> forumList = forumDao.queryTopic(page, pageSize);
		List<ForumTopicJson> forumJsonList = new ArrayList<ForumTopicJson>();
		for (ForumTopic forumTopic : forumList) {
			ForumTopicJson forumJson = new ForumTopicJson();
			forumJson.setImages(new ArrayList<String>());
			forumJson.setId(forumTopic.getId());
			forumJson.setContent(forumTopic.getContent());
			forumJson.setCreateTime(forumTopic.getCreateTime());
			forumJson.setModifyTime(forumTopic.getModifyTime());
			UserJson userJson = new UserJson();
			userJson.setId(forumTopic.getSubmitter().getId());
			userJson.setNickName(forumTopic.getSubmitter().getNickName());
			userJson.setMsisdn(forumTopic.getSubmitter().getMsisdn());
			forumJson.setSubmitter(userJson);
			forumJson.setTitle(forumTopic.getTitle());
			forumJsonList.add(forumJson);
			for(Image image:forumTopic.getImages()){
				forumJson.getImages().add(springProperty.getServerUrl()
					+ springProperty.getContextPath() + "/image.do?imagePath="
					+ image.getPath());
			}
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		return JSONArray.fromObject(forumJsonList, jsonConfig).toString();
	}

}
