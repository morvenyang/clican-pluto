/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cms.tag.api;

import java.util.Map;

public interface TagContainer {

	/**
	 * Add a new tag into <code>TagContainer</code>
	 * 
	 * @param tag
	 */
	public void addTag(String tagName, Tag tag);

	/**
	 * Remove a tag from <code>TagContainer</code>
	 * 
	 * @param tagName
	 */
	public void removeTag(String tagName);

	/**
	 * Update a existed tag, if the tag dones't exist just add it.
	 * 
	 * @param tag
	 */
	public void updateTag(String tagName, Tag tag);

	/**
	 * Refresh current <code>TagContainer</code> and load new configured Tag
	 * from some specified tag configuration file folder.
	 */
	public void refresh();

	/**
	 * Set the tag configuration file folder path. If it is null or this folder
	 * doesn't contain any configuration file, the <code>TagContainer</code>
	 * will just load all of default provided Tag by CMS.
	 * 
	 * @param tagConfigurationFilePath
	 */
	public void setTagConfigurationFilePath(String tagConfigurationFilePath);
	
	public Map<String,Tag> getTagMap();

}

// $Id$