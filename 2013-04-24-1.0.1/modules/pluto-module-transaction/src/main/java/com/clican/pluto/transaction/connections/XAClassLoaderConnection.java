/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction.connections;

import javax.transaction.TransactionManager;

import com.clican.pluto.common.exception.TransactionException;
import com.clican.pluto.transaction.resources.XAClassLoaderResource;

public class XAClassLoaderConnection implements XAConnection {

	private TransactionManager transactionManager;

	private XAClassLoaderResource classLoaderResource;

	public void connect() throws TransactionException {
		try {
			transactionManager.getTransaction().enlistResource(
					classLoaderResource);
		} catch (Exception e) {
			throw new TransactionException(e);
		}
	}

	public TransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	public XAClassLoaderResource getXAResource() {
		return classLoaderResource;
	}

	public void setClassLoaderResource(XAClassLoaderResource classLoaderResource) {
		this.classLoaderResource = classLoaderResource;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

}

// $Id$