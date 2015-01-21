package com.chinatelecom.xysq.service.impl;

import com.chinatelecom.xysq.dao.ForumDao;
import com.chinatelecom.xysq.service.ForumService;

public class ForumServiceImpl implements ForumService {

	private ForumDao forumDao;

	public void setForumDao(ForumDao forumDao) {
		this.forumDao = forumDao;
	}
	
	
}
