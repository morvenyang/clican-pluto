package com.chinatelecom.xysq.service;

import java.util.List;

import com.chinatelecom.xysq.model.Image;

public interface ForumService {

	public String queryTopic(Long communityId, int page, int pageSize);

	public String queryPost(Long topicId, int page, int pageSize);

	public Image getImage(byte[] data, String fileName);

	public void saveTopic(Long submitterId, Long communityId, Long topicId,
			String title, String content, List<Image> images);

	public void savePost(Long submitterId, Long topicId, Long postId, String content, String replyContent,
			List<Image> images);
}
