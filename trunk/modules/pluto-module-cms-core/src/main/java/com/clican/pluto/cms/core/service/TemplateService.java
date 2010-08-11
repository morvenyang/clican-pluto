/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.core.service;

import java.util.List;

import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.ITemplate;

public interface TemplateService {

	public List<ITemplate> getTemplates();

	public void save(ITemplate template);

	public ITemplate newTemplate();

	public List<ITemplate> getSelectedTemplates(IDataModel dataModel);

	public void delete(ITemplate template);

}

// $Id$