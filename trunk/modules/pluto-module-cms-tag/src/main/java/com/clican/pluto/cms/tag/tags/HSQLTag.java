/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.tag.tags;

import java.io.Serializable;
import java.util.List;

import com.clican.pluto.cms.dao.DataModelDao;
import com.clican.pluto.cms.dao.DirectoryDao;

public class HSQLTag extends BaseTag {

	private DirectoryDao directoryDao;

	private DataModelDao dataModelDao;

	public void setDirectoryDao(DirectoryDao directoryDao) {
		this.directoryDao = directoryDao;
	}

	public void setDataModelDao(DataModelDao dataModelDao) {
		this.dataModelDao = dataModelDao;
	}

	public Serializable findDirectory(String path) {
		return directoryDao.load(path);
	}

	public List<Serializable> query(String queryString) {
		return dataModelDao.query(queryString);
	}

}

// $Id$