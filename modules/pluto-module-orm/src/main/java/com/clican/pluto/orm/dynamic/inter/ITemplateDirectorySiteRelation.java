/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.dynamic.inter;

public interface ITemplateDirectorySiteRelation extends IPojo{

	public Long getId();

	public void setId(Long id);

	public ISite getSite();

	public void setSite(ISite site);

	public IDirectory getDirectory();

	public void setDirectory(IDirectory directory);

	public String getRelativePath();

	public void setRelativePath(String relativePath);

	public ITemplate getTemplate();

	public void setTemplate(ITemplate template);
}

// $Id$