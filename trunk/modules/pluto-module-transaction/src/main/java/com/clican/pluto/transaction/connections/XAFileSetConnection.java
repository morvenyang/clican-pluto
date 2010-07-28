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
import com.clican.pluto.transaction.resources.XAFileSetResource;

public class XAFileSetConnection implements XAConnection {

	private TransactionManager transactionManager;

	private XAFileSetResource fileSetResource;

	public void connect() throws TransactionException {
		try {
			transactionManager.getTransaction().enlistResource(fileSetResource);
		} catch (Exception e) {
			throw new TransactionException(e);
		}

	}

	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public XAFileSetResource getXAResource() {
		return fileSetResource;
	}

	public void setFileSetResource(XAFileSetResource fileSetResource) {
		this.fileSetResource = fileSetResource;
	}

}

// $Id$