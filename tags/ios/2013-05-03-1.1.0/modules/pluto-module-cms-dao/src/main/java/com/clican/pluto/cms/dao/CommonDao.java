/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao;

import java.util.Collection;

import com.clican.pluto.orm.dynamic.inter.IPojo;

public interface CommonDao {

	public Collection<IPojo> reloadPojos(Collection<IPojo> source);

	public IPojo[] reloadPojos(IPojo[] source);

	public IPojo reloadPojo(IPojo source);

}

// $Id$