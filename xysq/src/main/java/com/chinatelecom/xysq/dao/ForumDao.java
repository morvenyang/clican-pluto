package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.model.ForumPost;
import com.chinatelecom.xysq.model.ForumTopic;
import com.chinatelecom.xysq.model.Image;

public interface ForumDao {

	public List<ForumTopic> queryTopic(Long communityId, int page, int pageSize);

	public ForumTopic findTopicById(Long id);

	public void deleteImagesForTopic(Long topicId);

	public void deleteImagesForPost(Long postId);

	public void saveImage(Image image);

	public void saveForumTopic(ForumTopic forumTopic);

	public void saveForumPost(ForumPost forumPost);
}
