/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service;

import java.io.Serializable;

import com.clican.pluto.cms.dao.DirectoryDao;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

public interface DirectoryService {

	public void setDirectoryDao(DirectoryDao directoryDao);

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil);
	
	public IDirectory loadDirectory(String path);

	public IDirectory appendDirectory(IDirectory parent, String name);

	public IDirectory loadDirectory(Long id);

	public void save(Serializable directory);

	public void delete(Serializable directory);

}

// $Id$