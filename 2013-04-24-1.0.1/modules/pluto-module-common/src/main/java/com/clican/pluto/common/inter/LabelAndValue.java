/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.common.inter;

import java.io.Serializable;

public interface LabelAndValue extends Serializable {
	
	public Object getValue();

	public String getLabel();
}

// $Id$