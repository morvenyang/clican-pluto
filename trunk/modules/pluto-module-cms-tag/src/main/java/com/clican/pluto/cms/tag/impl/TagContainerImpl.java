/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cms.tag.impl;

import java.io.File;
import java.util.Map;

import com.clican.pluto.cms.tag.inter.Tag;
import com.clican.pluto.cms.tag.inter.TagContainer;

public class TagContainerImpl implements TagContainer {

	private Map<String, Tag> tagMap;

	private String tagConfigurationFilePath;

	public void addTag(String tagName, Tag tag) {
		tagMap.put(tagName, tag);
	}

	public void refresh() {
		@SuppressWarnings("unused")
		File file = new File(tagConfigurationFilePath);
		// TODO implement loading tag in runtime.
	}

	public void removeTag(String tagName) {
		tagMap.remove(tagName);
	}

	public void setTagConfigurationFilePath(String tagConfigurationFilePath) {
		this.tagConfigurationFilePath = tagConfigurationFilePath;
	}

	public void updateTag(String tagName, Tag tag) {
		tagMap.put(tagName, tag);
	}

	public void setTagMap(Map<String, Tag> tagMap) {
		this.tagMap = tagMap;
	}

	public Map<String, Tag> getTagMap() {
		return tagMap;
	}

}

// $Id$