/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.orm.dynamic.inter;

public interface ITemplateModelSiteRelation extends IPojo {

	public Long getId();

	public void setId(Long id);

	public ISite getSite();

	public void setSite(ISite site);

	public IDataModel getDataModel();

	public void setDataModel(IDataModel dataModel);

	public String getRelativePath();

	public void setRelativePath(String relativePath);

	public ITemplate getTemplate();

	public void setTemplate(ITemplate template);
}

// $Id$