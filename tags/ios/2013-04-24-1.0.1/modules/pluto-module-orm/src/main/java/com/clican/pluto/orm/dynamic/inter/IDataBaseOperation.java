/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import org.hibernate.cfg.Configuration;

import com.clican.pluto.orm.desc.ModelDescription;

public interface IDataBaseOperation {

	public void createTable(Configuration cfg);

	public void alterTable(Configuration cfg, ModelDescription oldOne,
			ModelDescription newOne);

	public void dropTable(ModelDescription modelDescription);

}

// $Id$