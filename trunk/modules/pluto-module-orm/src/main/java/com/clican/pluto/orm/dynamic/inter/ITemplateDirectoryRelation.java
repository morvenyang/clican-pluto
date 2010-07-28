/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

public interface ITemplateDirectoryRelation extends IPojo {

	public Long getId();

	public void setId(Long id);

	public IDirectory getDirectory();

	public void setDirectory(IDirectory directory);

	public ITemplate getTemplate();

	public void setTemplate(ITemplate template);
}

// $Id$