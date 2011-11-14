/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.ui.action.issue;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.cms.core.service.SiteService;
import com.clican.pluto.cms.ui.action.BaseAction;
import com.clican.pluto.orm.dynamic.inter.ClassLoaderUtil;
import com.clican.pluto.orm.dynamic.inter.ISite;

@Scope(ScopeType.PAGE)
@Name("siteAction")
public class SiteAction extends BaseAction {

    @In("#{siteService}")
    private SiteService siteService;

    @In("#{classLoaderUtil}")
    private ClassLoaderUtil classLoaderUtil;

    private ISite site;

    @Out(required = false)
    private List<ISite> siteList;

    @Factory("siteList")
    public void loadSiteList() {
        siteList = siteService.getAllSites();
    }

    public void newSite() {
        site = classLoaderUtil.newSite();
    }

    public void save() {
        siteService.save(site);
        siteList = siteService.getAllSites();
    }

    public void delete(ISite site) {
        siteService.delete(site);
        siteList = siteService.getAllSites();
    }

    public ISite getSite() {
        return site;
    }

    public void setSite(ISite site) {
        this.site = site;
    }

}

// $Id$