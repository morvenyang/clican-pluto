/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.SiteService;
import com.clican.pluto.cms.dao.SiteDao;
import com.clican.pluto.orm.dynamic.inter.ISite;

public class SiteServiceImpl implements SiteService {

    private SiteDao siteDao;

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }

    @Transactional(readOnly = true)
    public List<ISite> getAllSites() {
        return siteDao.getAllSites();
    }

    @Transactional
    public void save(ISite site) {
        siteDao.save(site);
    }

    @Transactional
    public void delete(ISite site) {
        siteDao.delete(site);
    }

}

// $Id$