/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.resource;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * This class<code>ClassPathResource</code> represents 
 * the class path resource whose path starts with "classpath:".
 * @author wei.zhang
 *
 */
public class ClassPathResource implements Resource {

	public final static String CLASS_PATH_RESOURCE_PREFIX = "classpath:";

	private String classPath;

	private URL resource;

	public ClassPathResource(String classPath) {
		if (StringUtils.isEmpty(classPath)) {
			throw new IllegalArgumentException("classPath cannot be null");
		}
		if (!classPath.startsWith(CLASS_PATH_RESOURCE_PREFIX)) {
			this.classPath = classPath;
		} else {
			this.classPath = classPath.substring(CLASS_PATH_RESOURCE_PREFIX.length());
		}
		resource = Thread.currentThread().getContextClassLoader().getResource(this.classPath);
		if (resource == null) {
			throw new IllegalArgumentException("resource cannot be found");
		}
	}

	public InputStream getInputStream() throws FileNotFoundException {
		try {
			InputStream is = resource.openStream();
			return is;
		} catch (Exception e) {
			throw new FileNotFoundException(e.getMessage());
		}
	}

	public OutputStream getOutputStream() throws FileNotFoundException {
		throw new FileNotFoundException("Classpath resource无法获得OutputStream");
	}

}

// $Id$