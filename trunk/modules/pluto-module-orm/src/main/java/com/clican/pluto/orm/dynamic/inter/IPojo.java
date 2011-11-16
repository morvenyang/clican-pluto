/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import java.io.Serializable;

public interface IPojo extends Serializable {

	public Long getId();

	public void setId(Long id);

}

// $Id$