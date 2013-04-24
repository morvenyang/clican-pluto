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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AutoDecisionResource implements Resource {

	private final static Log log = LogFactory.getLog(AutoDecisionResource.class);

	private Resource resource;

	public AutoDecisionResource(String resource) {
		if (StringUtils.isEmpty(resource)) {
			new IllegalArgumentException("resource cannot be null");
		}
		if (log.isInfoEnabled()) {
			log.info("load resource [" + resource + "]");
		}
		if (resource.startsWith(ClassPathResource.CLASS_PATH_RESOURCE_PREFIX)) {
			if (log.isInfoEnabled()) {
				log.info("load resource [" + resource + "] from class path");
			}
			this.resource = new ClassPathResource(resource);
		} else if (resource.startsWith(FilePathResource.FILE_PATH_RESOURCE_PREFIX)) {
			if (log.isInfoEnabled()) {
				log.info("load resource [" + resource + "] from file path");
			}
			this.resource = new FilePathResource(resource);
		} else {
			try {
				this.resource = new ClassPathResource(resource);
			} catch (Exception e) {
				log.debug("This is not a class path resource, try to find it in file path resource");
				this.resource = new FilePathResource(resource);
			}
		}
		if (this.resource == null) {
			throw new IllegalArgumentException("resource cannot be found");
		}
	}

	public InputStream getInputStream() throws FileNotFoundException {
		return resource.getInputStream();
	}

	public OutputStream getOutputStream() throws FileNotFoundException {
		return resource.getOutputStream();
	}

}

// $Id$