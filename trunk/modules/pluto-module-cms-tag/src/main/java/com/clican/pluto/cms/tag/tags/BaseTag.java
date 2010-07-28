/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cms.tag.tags;

import org.apache.velocity.VelocityContext;

import com.clican.pluto.cms.tag.inter.Tag;

public abstract class BaseTag implements Tag {

	/**
	 * The <code>VelocityContext must be set before the Tag is used.</code>
	 */
	public static ThreadLocal<VelocityContext> VELOCITY_CONTEXT_THREAD_LOCAL = new ThreadLocal<VelocityContext>();

	protected VelocityContext getVelocityContext() {
		return VELOCITY_CONTEXT_THREAD_LOCAL.get();
	}

}

// $Id$