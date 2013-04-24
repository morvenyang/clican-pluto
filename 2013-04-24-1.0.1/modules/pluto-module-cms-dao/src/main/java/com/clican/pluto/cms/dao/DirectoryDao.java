/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao;

import com.clican.pluto.orm.dynamic.inter.IDirectory;

public interface DirectoryDao extends Dao {

	public IDirectory load(String path);

	public IDirectory load(Long id);

	public Object[] getDirectoryModelCount(Long id);

}

// $Id$