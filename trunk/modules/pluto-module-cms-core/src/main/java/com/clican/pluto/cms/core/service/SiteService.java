/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.core.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.net.SocketClient;
import org.apache.commons.net.ftp.FTPClient;

import com.clican.pluto.orm.dynamic.inter.ISite;

public interface SiteService {

	public List<ISite> getAllSites();

	public void save(ISite site);

	public void delete(ISite site);

	public void writeFile(FTPClient client, String relativePath, InputStream is)
			throws IOException;

	public void writeFile(File file, String relativePath, InputStream is)
			throws IOException;

	public SocketClient getClient(ISite site) throws IOException;
}

// $Id$