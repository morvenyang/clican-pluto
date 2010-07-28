/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import java.util.Calendar;

public interface IDataModel extends IPojo {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public Calendar getCreateTime();

	public void setCreateTime(Calendar createTime);

	public Calendar getUpdateTime();

	public void setUpdateTime(Calendar updateTime);

	public IDirectory getParent();

	public void setParent(IDirectory parent);
}

// $Id$