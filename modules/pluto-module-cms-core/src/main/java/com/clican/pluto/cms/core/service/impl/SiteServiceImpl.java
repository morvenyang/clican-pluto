/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author zhangwei
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.SiteService;
import com.clican.pluto.cms.dao.SiteDao;
import com.clican.pluto.orm.dynamic.inter.ISite;

public class SiteServiceImpl implements SiteService {

	private final static Log log = LogFactory.getLog(SiteServiceImpl.class);

	private SiteDao siteDao;

	public void setSiteDao(SiteDao siteDao) {
		this.siteDao = siteDao;
	}

	@Transactional(readOnly = true)
	public List<ISite> getAllSites() {
		return siteDao.getAllSites();
	}

	@Transactional
	public void save(ISite site) {
		siteDao.save(site);
	}

	@Transactional
	public void delete(ISite site) {
		siteDao.delete(site);
	}

	public SocketClient getClient(ISite site) throws IOException {
		String url = site.getUrl();
		if (url == null) {
			return null;
		}
		if (url.startsWith("ftp://")) {
			int port = 21;
			String hostname = null;
			try {
				url = url.substring(6);
				if (url.indexOf(":") != -1) {
					hostname = url.substring(0, url.indexOf(":"));
					if (url.endsWith("/")) {
						port = Integer.parseInt(url.substring(
								url.indexOf(":") + 1, url.length() - 1));
					} else {
						port = Integer
								.parseInt(url.substring(url.indexOf(":") + 1));
					}
				} else {
					if (url.endsWith("/")) {
						hostname = url.substring(0, url.length() - 1);
					} else {
						hostname = url;
					}
				}
			} catch (Exception e) {
				throw new UnknownHostException(url);
			}
			FTPClient client = new FTPClient();
			client.connect(hostname, port);
			if (log.isDebugEnabled()) {
				log.debug("Connected to " + url + ".");
				log.debug(client.getReplyString());
			}
			int reply = client.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				client.disconnect();
				log.warn("FTP server " + url + " refused connection.");
				return null;
			}
			if (StringUtils.isNotEmpty(site.getUsername())) {
				client.login(site.getUsername(), site.getPassword());
			}

			return client;
		}
		return null;
	}

	public void writeFile(FTPClient client, String relativePath, InputStream is)
			throws IOException {
		OutputStream os = client.appendFileStream(relativePath);
		writeFile(os, is);
	}

	public void writeFile(File file, String relativePath, InputStream is)
			throws IOException {
		File outputFile = new File(file, relativePath);
		writeFile(new FileOutputStream(outputFile), is);
	}

	private void writeFile(OutputStream os, InputStream is) throws IOException {
		try {
			IOUtils.copy(is, os);
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
			try {
				is.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}

// $Id$