/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction.resources;

import javax.transaction.xa.XAResource;

public interface XAClassLoaderResource extends XAResource {

	public Class<?> loadClass(String name) throws ClassNotFoundException;

	public void refreshClasses() throws ClassNotFoundException;

}

// $Id$