/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.transaction;

import java.io.File;

import javax.sql.XADataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.AbstractDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.jmock.Mockery;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.clican.pluto.common.util.FileUtils;

public abstract class BaseTestCase extends
		AbstractDependencyInjectionSpringContextTests {

	protected Log log = LogFactory.getLog(getClass());

	protected Mockery context = new Mockery();

	protected IDatabaseTester databaseTester1;

	protected IDatabaseTester databaseTester2;

	protected XADataSource xaDataSource1;

	protected XADataSource xaDataSource2;
	

	public void setXaDataSource1(XADataSource xaDataSource1) {
		this.xaDataSource1 = xaDataSource1;
	}

	public void setXaDataSource2(XADataSource xaDataSource2) {
		this.xaDataSource2 = xaDataSource2;
	}
	
	

	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:META-INF/transaction-*.xml" };
	}

	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		try {

			databaseTester1 = new AbstractDatabaseTester() {
				public IDatabaseConnection getConnection() throws Exception {
					return new DatabaseConnection(xaDataSource1
							.getXAConnection().getConnection());
				}
			};

			databaseTester2 = new AbstractDatabaseTester() {
				public IDatabaseConnection getConnection() throws Exception {
					return new DatabaseConnection(xaDataSource2
							.getXAConnection().getConnection());
				}
			};
			databaseTester1.setSetUpOperation(DatabaseOperation.DELETE_ALL);
			databaseTester1.setDataSet(new XmlDataSet(getClass()
					.getResourceAsStream("/transaction-test-data.xml")));
			databaseTester2.setSetUpOperation(DatabaseOperation.DELETE_ALL);
			databaseTester2.setDataSet(new XmlDataSet(getClass()
					.getResourceAsStream("/transaction-test-data.xml")));
			// will call default setUpOperation
			databaseTester1.onSetup();
			databaseTester2.onSetup();
			File file = new File("c:\\fileres");
			FileUtils.recurDelete(file);
			file.mkdirs();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

	@Override
	protected void onTearDown() throws Exception {
		super.onTearDown();
		databaseTester1.onTearDown();
		databaseTester2.onTearDown();
	}

}

// $Id$