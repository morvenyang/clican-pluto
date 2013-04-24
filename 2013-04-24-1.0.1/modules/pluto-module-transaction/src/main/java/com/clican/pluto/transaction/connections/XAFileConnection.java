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
import com.clican.pluto.transaction.resources.XAFileResource;

public class XAFileConnection implements XAConnection {

	private TransactionManager transactionManager;

	private XAFileResource fileResource;

	public void connect() throws TransactionException {
		try {
			transactionManager.getTransaction().enlistResource(fileResource);
		} catch (Exception e) {
			throw new TransactionException(e);
		}
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public TransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	public XAFileResource getXAResource() {
		return this.fileResource;
	}

	public void setFileResource(XAFileResource fileResource) {
		this.fileResource = fileResource;
	}

	

}

// $Id$