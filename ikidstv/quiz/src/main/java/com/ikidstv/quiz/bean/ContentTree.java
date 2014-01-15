package com.ikidstv.quiz.bean;

import java.util.ArrayList;
import java.util.List;

public class ContentTree {

	private String name;
	private String contentId;
	private String seasonId;
	private String episonId;
	private List<ContentTree> subTree = new ArrayList<ContentTree>();
	private boolean subNode;
	private boolean seasonNode;
	private boolean episodeNode;
	private ContentTree parent;
	private String backgroundImage;
	private String frontImage;
	
	public List<ContentTree> getSubTree() {
		return subTree;
	}
	public void setSubTree(List<ContentTree> subTree) {
		this.subTree = subTree;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public boolean isSubNode() {
		return subNode;
	}
	public void setSubNode(boolean subNode) {
		this.subNode = subNode;
	}
	public boolean isSeasonNode() {
		return seasonNode;
	}
	public void setSeasonNode(boolean seasonNode) {
		this.seasonNode = seasonNode;
	}
	public boolean isEpisodeNode() {
		return episodeNode;
	}
	public void setEpisodeNode(boolean episodeNode) {
		this.episodeNode = episodeNode;
	}
	public String getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}
	public String getEpisonId() {
		return episonId;
	}
	public void setEpisonId(String episonId) {
		this.episonId = episonId;
	}
	public ContentTree getParent() {
		return parent;
	}
	public void setParent(ContentTree parent) {
		this.parent = parent;
	}
	public String getBackgroundImage() {
		return backgroundImage;
	}
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	public String getFrontImage() {
		return frontImage;
	}
	public void setFrontImage(String frontImage) {
		this.frontImage = frontImage;
	}
	
	
}
