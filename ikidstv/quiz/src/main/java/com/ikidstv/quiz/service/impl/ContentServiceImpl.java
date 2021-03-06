package com.ikidstv.quiz.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.ikidstv.quiz.bean.ContentTree;
import com.ikidstv.quiz.dao.ContentDao;
import com.ikidstv.quiz.service.ContentService;
import com.ikidstv.quiz.smit.model.Episode;
import com.ikidstv.quiz.smit.model.Season;

public class ContentServiceImpl implements ContentService {

	private ContentDao contentDao;

	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}

	public ContentTree getRootContentTree2() {
		ContentTree root = new ContentTree();
		root.setName("iKidsTV");
		root.setContentId("1");

		ContentTree season2 = new ContentTree();
		season2.setName("Jake and the Never Land Pirates");
		season2.setContentId("2");
		season2.setSeasonId("2");
		season2.setSeasonNode(true);
		root.getSubTree().add(season2);

		ContentTree season3 = new ContentTree();
		season3.setName("Octonauts");
		season3.setContentId("3");
		season3.setSeasonId("3");
		season3.setSeasonNode(true);
		root.getSubTree().add(season3);

		ContentTree season = new ContentTree();
		season.setName("Mickey Mouse Clubhouse S1");
		season.setContentId("4");
		season.setSeasonId("4");
		season.setSeasonNode(true);
		root.getSubTree().add(season);

		ContentTree e1 = new ContentTree();
		e1.setName("Mickey Mouse Clubhouse S1#1");
		e1.setContentId("5");
		e1.setSeasonId("4");
		e1.setEpisonId("5");
		e1.setEpisodeNode(true);
		e1.setParent(season);
		season.getSubTree().add(e1);

		ContentTree e2 = new ContentTree();
		e2.setName("Mickey Mouse Clubhouse S1#2");
		e2.setContentId("6");
		e2.setSeasonId("4");
		e2.setEpisonId("6");
		e2.setEpisodeNode(true);
		e2.setParent(season);
		season.getSubTree().add(e2);

		ContentTree e3 = new ContentTree();
		e3.setName("Mickey Mouse Clubhouse S1#3");
		e3.setContentId("7");
		e3.setSeasonId("4");
		e3.setEpisonId("7");
		e3.setEpisodeNode(true);
		e3.setParent(season);
		season.getSubTree().add(e3);
		return root;
	}

	private ContentTree getRootContentTree() {
		List<Episode> episodes = contentDao.findAllEpisodes();
		ContentTree root = new ContentTree();
		root.setName("iKidsTV");
		root.setContentId("1");
		Map<String, ContentTree> seasonMap = new HashMap<String, ContentTree>();
		for (Episode episode : episodes) {
			Season season = episode.getSeason();
			ContentTree seasonNode = null;
			if(seasonMap.containsKey(season.getId())){
				seasonNode = seasonMap.get(season.getId());
			}else{
				seasonNode = new ContentTree();
				seasonNode.setName(season.getName());
				seasonNode.setContentId(season.getId());
				seasonNode.setSeasonId(season.getId());
				seasonNode.setSeasonNode(true);
				seasonMap.put(season.getId(), seasonNode);
				root.getSubTree().add(seasonNode);
			}
			ContentTree episodeNode = new ContentTree();
			episodeNode.setName(episode.getName());
			episodeNode.setContentId(episode.getGuid());
			episodeNode.setSeasonId(season.getId());
			episodeNode.setEpisonId(episode.getGuid());
			episodeNode.setEpisodeNode(true);
			episodeNode.setParent(seasonNode);
			seasonNode.getSubTree().add(episodeNode);
		}
		return root;
	}

	private void buildContentMap(Map<String, ContentTree> contentMap,
			ContentTree contentTree) {
		contentMap.put(contentTree.getContentId(), contentTree);
		for (ContentTree sub : contentTree.getSubTree()) {
			buildContentMap(contentMap, sub);
		}
	}

	public ContentTree getContentTree() {
		return getRootContentTree();
	}

	public Set<String> findEpisodeIds(String contentId) {
		ContentTree contentTree = null;
		if (StringUtils.isEmpty(contentId)) {
			contentTree = getRootContentTree();
		} else {
			Map<String,ContentTree> contentMap = new HashMap<String,ContentTree>();
			buildContentMap(contentMap,getRootContentTree());
			contentTree = contentMap.get(contentId);
		}
		Set<String> contentIds = new HashSet<String>();
		this.findEpisodeIds(contentIds, contentTree);
		return contentIds;
	}

	private void findEpisodeIds(Set<String> contentIds, ContentTree contentTree) {
		if (contentTree.isEpisodeNode()) {
			contentIds.add(contentTree.getContentId());
		} else {
			for (ContentTree sub : contentTree.getSubTree()) {
				findEpisodeIds(contentIds, sub);
			}
		}
	}

}
