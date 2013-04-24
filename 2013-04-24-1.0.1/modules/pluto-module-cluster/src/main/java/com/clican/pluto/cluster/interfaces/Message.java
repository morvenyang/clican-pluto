/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cluster.interfaces;

import java.io.Serializable;

/**
 * All of data synchronized by the <code>Message</code> between all of cluster
 * nodes.
 * 
 * @author clican
 * 
 */
public interface Message extends Serializable {

	public String getName();

}

// $Id$