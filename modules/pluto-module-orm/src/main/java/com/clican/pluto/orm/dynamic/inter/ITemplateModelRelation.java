/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

public interface ITemplateModelRelation extends IPojo {

	public Long getId();

	public void setId(Long id);

	public IDataModel getDataModel();

	public void setDataModel(IDataModel dataModel);

	public ITemplate getTemplate();

	public void setTemplate(ITemplate template);

}

// $Id$