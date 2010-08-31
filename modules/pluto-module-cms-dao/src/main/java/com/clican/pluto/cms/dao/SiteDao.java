/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.dao;

import java.util.List;

import com.clican.pluto.orm.dynamic.inter.ISite;

public interface SiteDao extends Dao {

    public List<ISite> getAllSites();
}


//$Id$