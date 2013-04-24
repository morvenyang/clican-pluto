/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.support.spring;

import org.springframework.core.io.Resource;

public class PropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	private Resource[] locations;

	public Resource[] getLocations() {
		return locations;
	}

	public void setLocations(Resource[] locations) {
		super.setLocations(locations);
		this.locations = locations;
	}

}

// $Id$