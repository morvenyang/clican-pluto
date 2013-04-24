/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction.resources;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

public interface XAFileResource extends XAResource {

	public OutputStream getOutputStream() throws XAException,
			FileNotFoundException;

	public InputStream getInputStream() throws XAException,
			FileNotFoundException;

}

// $Id$