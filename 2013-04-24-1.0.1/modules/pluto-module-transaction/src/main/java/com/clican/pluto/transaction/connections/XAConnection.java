/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction.connections;

import java.sql.SQLException;

import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

import com.clican.pluto.common.exception.TransactionException;

/**
 * Actually I think the JTA shall provide such a kind of XAConnection interface.
 * 
 * I think this kinds of interface will be used not only in
 * <code>javax.sql.XAConnection</code>.
 * 
 * Any kind of XA operations shall all use a XAConnection to enlist the
 * XAResource into the <code>TransactionManager</code>
 * 
 * 
 * @author clican
 * 
 */
public interface XAConnection {

	/**
	 * Retrieves an <code>XAResource</code> object that the transaction manager
	 * will use to manage this <code>XAConnection</code> object's participation
	 * in a distributed transaction.
	 * 
	 * @return the <code>XAResource</code> object
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public XAResource getXAResource();

	/**
	 * Every XAConnection can access the <code>TransactionManager</code>
	 * 
	 * @return
	 */
	public TransactionManager getTransactionManager();

	/**
	 * 
	 * Begin the connection and enlist the XAResource into the transaction
	 * manager.
	 * 
	 * @throws TransactionException
	 */
	public void connect() throws TransactionException;

}

// $Id$