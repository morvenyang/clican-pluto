/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.dao;

import java.io.Serializable;

public interface CommonDao {

	public Serializable save(Object model);

	public void delete(Object model);

	public void update(Object model);

	public void merge(Object model);

}

// $Id$