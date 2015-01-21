package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.model.ForumTopic;

public interface ForumDao {

	public List<ForumTopic> queryTopic(int page, int pageSize);
}
