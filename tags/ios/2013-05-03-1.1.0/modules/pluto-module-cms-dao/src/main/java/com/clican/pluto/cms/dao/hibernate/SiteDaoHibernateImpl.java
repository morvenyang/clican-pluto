/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.dao.hibernate;

import java.util.List;

import com.clican.pluto.cms.dao.SiteDao;
import com.clican.pluto.orm.dynamic.inter.ISite;

public class SiteDaoHibernateImpl extends BaseDao implements SiteDao {

    @SuppressWarnings("unchecked")
    public List<ISite> getAllSites() {
        return this.getHibernateTemplate().find("from Site");
    }

}


//$Id$