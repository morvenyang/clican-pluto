/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.DirectoryService;
import com.clican.pluto.cms.dao.DirectoryDao;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

public class DirectoryServiceImpl extends BaseService implements
		DirectoryService {

	private DirectoryDao directoryDao;

	private ClassLoaderUtil classLoaderUtil;

	public void setDirectoryDao(DirectoryDao directoryDao) {
		this.directoryDao = directoryDao;
	}

	public void setClassLoaderUtil(ClassLoaderUtil classLoaderUtil) {
		this.classLoaderUtil = classLoaderUtil;
	}

	@Transactional
	public void delete(Serializable directory) {
		directoryDao.delete(directory);
	}

	@Transactional(readOnly = true)
	public IDirectory loadDirectory(String path) {
		return directoryDao.load(path);
	}

	@Transactional
	public void save(Serializable directory) {
		directoryDao.save(directory);
	}

	@Transactional
	public IDirectory appendDirectory(IDirectory parent, String name) {
		IDirectory directory = classLoaderUtil.newDirectory(parent, name);
		directoryDao.save(directory);
		return directory;
	}

	@Transactional(readOnly = true)
	public IDirectory loadDirectory(Long id) {
		return null;
	}

}

// $Id$