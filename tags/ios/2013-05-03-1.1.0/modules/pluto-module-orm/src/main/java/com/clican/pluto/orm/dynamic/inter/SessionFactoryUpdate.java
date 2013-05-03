/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import org.hibernate.cfg.Configuration;

import com.clican.pluto.common.exception.PlutoException;

public interface SessionFactoryUpdate {

	/**
	 * Create a new <code>SessionFactory</code> object and replace current used
	 * <code>SessionFactory</code>
	 * 
	 * <p>
	 * As we use the Spring Framework to manage the Hibernate transaction, so we
	 * must use a SessionFactory wrap class which will invoke the <b>real</b>
	 * <code>SessionFactory</code>. This wrap class can easily replace the
	 * <b>real</b> <code>SessionFactory</code> without impacting Spring bean
	 * reference.
	 * 
	 * @throws Exception
	 */
	public Configuration update() throws PlutoException;

}

// $Id: SessionFactoryUpdate.java 520 2009-06-15 06:37:26Z clican $