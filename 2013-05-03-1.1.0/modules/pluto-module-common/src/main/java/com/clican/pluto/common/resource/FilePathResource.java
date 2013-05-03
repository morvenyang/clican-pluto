/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;

/**
 * This class<code>FilePathResource</code> represents the 
 * file path resource whose file path starts with "file:".
 * @author wei.zhang
 *
 */
public class FilePathResource implements Resource {

	public final static String FILE_PATH_RESOURCE_PREFIX = "file:";

	private String filePath;

	private File resource;

	public FilePathResource(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			throw new IllegalArgumentException("filePath cannot be null");
		}
		if (!filePath.startsWith(FILE_PATH_RESOURCE_PREFIX)) {
			this.filePath = filePath;
		} else {
			this.filePath = filePath.substring(FILE_PATH_RESOURCE_PREFIX.length());
		}
		resource = new File(this.filePath);
	}

	public InputStream getInputStream() throws FileNotFoundException {
		InputStream is = new FileInputStream(resource);
		return is;
	}

	public OutputStream getOutputStream() throws FileNotFoundException {
		OutputStream os = new FileOutputStream(resource);
		return os;
	}

}

// $Id$