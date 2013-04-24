/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import javax.transaction.UserTransaction;

import com.clican.pluto.transaction.connections.XAFileSetConnection;
import com.clican.pluto.transaction.resources.XAFileSetResource;

public class DistributedTransactionTestCase extends BaseTestCase {

	private UserTransaction userTransaction;

	private XAFileSetConnection xaFileSetConnection;

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

	public void setXaFileSetConnection(XAFileSetConnection xaFileSetConnection) {
		this.xaFileSetConnection = xaFileSetConnection;
	}

	public void testDBAndFileDistributedTransaction() throws Exception {
		Connection dbConn1 = xaDataSource1.getXAConnection().getConnection();
		Statement stat1 = null;
		XAFileSetResource fileSetResource = (XAFileSetResource) xaFileSetConnection
				.getXAResource();
		try {
			userTransaction.begin();
			stat1 = dbConn1.createStatement();
			xaFileSetConnection.connect();
			fileSetResource.getOutputStream(new File("c:\\fileres\\a.txt"))
					.write("test".getBytes());
			stat1.execute("insert into Test1(id,name) values(1,'abc')");
			userTransaction.commit();
		} catch (Exception e) {
			log.error("",e);
			userTransaction.rollback();
		} finally {
			if (dbConn1 != null) {
				dbConn1.close();
			}
		}
		int row = this.databaseTester1.getConnection().getRowCount("Test1");
		assertEquals(1, row);
	}

	public void testDBAndDBDistributedTransaction() throws Exception {
		Connection dbConn1 = xaDataSource1.getXAConnection().getConnection();
		Statement stat1 = null;
		Connection dbConn2 = xaDataSource2.getXAConnection().getConnection();
		Statement stat2 = null;
		try {
			userTransaction.begin();
			stat1 = dbConn1.createStatement();
			stat2 = dbConn2.createStatement();
			stat1.execute("insert into Test1(id,name) values(1,'abc')");
			stat2.execute("insert into Test1(id,name) values(1,'abc')");
			stat2.execute("select 1 from Test2");
			userTransaction.commit();
		} catch (Exception e) {
			userTransaction.rollback();
		} finally {
			if (dbConn1 != null) {
				dbConn1.close();
			}
			if (dbConn2 != null) {
				dbConn2.close();
			}
		}
		int row1 = this.databaseTester1.getConnection().getRowCount("Test1");
		int row2 = this.databaseTester2.getConnection().getRowCount("Test1");
		assertEquals(0, row1);
		assertEquals(0, row2);
	}

}

// $Id$