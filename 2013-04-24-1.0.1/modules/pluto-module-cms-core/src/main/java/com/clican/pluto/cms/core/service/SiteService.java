/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.core.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

import com.clican.pluto.orm.dynamic.inter.ISite;

public interface SiteService {

	public List<ISite> getAllSites();

	public void save(ISite site);

	public void delete(ISite site);

	public FTPClient getFTPClient(ISite site) throws IOException;
}

// $Id$