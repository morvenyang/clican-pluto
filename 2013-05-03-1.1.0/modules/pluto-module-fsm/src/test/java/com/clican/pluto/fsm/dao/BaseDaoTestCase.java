/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public abstract class BaseDaoTestCase extends
		AbstractTransactionalSpringContextTests {

	protected Log log = LogFactory.getLog(this.getClass());

	protected IDatabaseTester databaseTester;

	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:META-INF/spring-fsm-test-dao.xml",
				"classpath*:META-INF/spring-fsm-test-context.xml" };
	}

	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		try {
			databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver",
					"jdbc:hsqldb:.", "sa", "");

			// initialize your dataset here
			IDataSet dataSet = getDataSet();
			if (dataSet != null) {
				databaseTester.setDataSet(dataSet);
			}
			// will call default setUpOperation
			databaseTester.onSetup();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

	@Override
	protected void onTearDownAfterTransaction() throws Exception {
		super.onTearDownAfterTransaction();
		try {
			databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
			databaseTester.onTearDown();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

	protected IDataSet getDataSet() throws Exception {
		IDataSet data = new XmlDataSet(getClass().getResourceAsStream(
				"/data/default.xml"));
		return data;
	}

}

// $Id$