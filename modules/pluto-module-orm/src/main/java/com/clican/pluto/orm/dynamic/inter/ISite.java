/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import java.util.Set;

public interface ISite extends IPojo {

    public Long getId();

    public void setId(Long id);

    public String getName();

    public void setName(String name);

    public String getUrl();

    public void setUrl(String url);

    public String getUsername();

    public void setUsername(String username);

    public String getPassword();

    public void setPassword(String password);

    public Set<ITemplateDirectorySiteRelation> getTemplateDirectorySiteRelationSet();

    public void setTemplateDirectorySiteRelationSet(Set<ITemplateDirectorySiteRelation> templateDirectorySiteRelationSet);
}

// $Id$