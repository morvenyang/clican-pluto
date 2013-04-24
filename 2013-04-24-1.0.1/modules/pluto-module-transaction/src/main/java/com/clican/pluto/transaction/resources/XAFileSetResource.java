/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

public interface XAFileSetResource extends XAResource {

	public OutputStream getOutputStream(File file) throws XAException,
			FileNotFoundException;

	public InputStream getInputStream(File file) throws XAException,
			FileNotFoundException;

	public void delete(File file) throws XAException;

}

// $Id$