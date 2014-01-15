package com.ikidstv.quiz.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.ikidstv.quiz.bean.ContentTree;
import com.ikidstv.quiz.service.ContentService;

public class ContentServiceImpl implements ContentService {

	private ContentTree root;
	
	private Map<String,ContentTree> contentMap;

	public ContentServiceImpl() {
		root = new ContentTree();
		root.setName("iKidsTV");
		root.setContentId("1");

		ContentTree tree1 = new ContentTree();
		tree1.setName("TV");
		tree1.setContentId("2");
		root.getSubTree().add(tree1);

		ContentTree tree2 = new ContentTree();
		tree2.setName("Cartoon");
		tree2.setContentId("3");
		root.getSubTree().add(tree2);

		ContentTree season = new ContentTree();
		season.setName("Mickey garden");
		season.setContentId("4");
		season.setSeasonId("4");
		season.setSeasonNode(true);
		tree2.getSubTree().add(season);

		ContentTree e1 = new ContentTree();
		e1.setName("Mickey garden#1");
		e1.setContentId("5");
		e1.setSeasonId("4");
		e1.setEpisonId("5");
		e1.setEpisodeNode(true);
		e1.setParent(season);
		season.getSubTree().add(e1);

		ContentTree e2 = new ContentTree();
		e2.setName("Mickey garden#2");
		e2.setContentId("6");
		e2.setSeasonId("4");
		e2.setEpisonId("6");
		e2.setEpisodeNode(true);
		e2.setParent(season);
		season.getSubTree().add(e2);

		ContentTree e3 = new ContentTree();
		e3.setName("Mickey garden#3");
		e3.setContentId("7");
		e3.setSeasonId("4");
		e3.setEpisonId("7");
		e3.setEpisodeNode(true);
		e3.setParent(season);
		season.getSubTree().add(e3);
		contentMap = new HashMap<String,ContentTree>();
		this.buildContentMap(contentMap, root);
	}

	private void buildContentMap(Map<String,ContentTree> contentMap,ContentTree contentTree){
		contentMap.put(contentTree.getContentId(), contentTree);
		for(ContentTree sub:contentTree.getSubTree()){
			buildContentMap(contentMap,sub);
		}
	}
	
	public ContentTree getContentTree() {
		return root;
	}

	public Set<String> findEpisodeIds(String contentId) {
		ContentTree contentTree = null;
		if(StringUtils.isEmpty(contentId)){
			contentTree = root;
		}else{
			contentTree = contentMap.get(contentId);
		}
		Set<String> contentIds = new HashSet<String>();
		this.findEpisodeIds(contentIds, contentTree);
		return contentIds;
	}

	private void findEpisodeIds(Set<String> contentIds, ContentTree contentTree) {
		if(contentTree.isEpisodeNode()){
			contentIds.add(contentTree.getContentId());
		}else{
			for(ContentTree sub:contentTree.getSubTree()){
				findEpisodeIds(contentIds,sub);
			}
		}
	}

}
