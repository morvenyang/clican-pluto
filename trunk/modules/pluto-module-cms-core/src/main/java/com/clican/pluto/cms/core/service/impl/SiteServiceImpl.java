/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.core.service.impl;

import com.clican.pluto.cms.core.service.SiteService;
import com.clican.pluto.cms.dao.SiteDao;

public class SiteServiceImpl implements SiteService {

    private SiteDao siteDao;

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }

}

// $Id$