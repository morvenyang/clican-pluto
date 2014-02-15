package com.ikidstv.quiz.service;

import java.util.Set;

import com.ikidstv.quiz.bean.ContentTree;

public interface ContentService {

	public ContentTree getContentTree();
	
	public Set<String> findEpisodeIds(String contentId);

}
