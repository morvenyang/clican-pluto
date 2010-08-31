/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.ui.action;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.clican.pluto.orm.dynamic.inter.ISite;

@Scope(ScopeType.PAGE)
@Name("siteAction")
public class SiteAction extends BaseAction {

    private ISite site;
    
    private List<ISite> siteList;
    
    
}

// $Id$