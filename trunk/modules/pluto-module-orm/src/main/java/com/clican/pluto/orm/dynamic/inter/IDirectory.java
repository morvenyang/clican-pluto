/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import java.util.Calendar;
import java.util.Set;

public interface IDirectory extends IDataModel {
	
	public Long getId();

	public void setId(Long id);

	public IDirectory getParent();

	public void setParent(IDirectory parent);

	public Set<IDirectory> getChildren();

	public void setChildren(Set<IDirectory> children);
	
	public Set<ITemplateDirectorySiteRelation> getTemplateDirectorySiteRelationSet();

    public void setTemplateDirectorySiteRelationSet(Set<ITemplateDirectorySiteRelation> templateDirectorySiteRelationSet);

	public String getPath();

	public void setPath(String path);

	public String getName();

	public void setName(String name);

	public IDirectory getReference();

	public void setReference(IDirectory reference);

	public Calendar getCreateTime();

	public void setCreateTime(Calendar createTime);

	public Calendar getUpdateTime();

	public void setUpdateTime(Calendar updateTime);
}

// $Id$