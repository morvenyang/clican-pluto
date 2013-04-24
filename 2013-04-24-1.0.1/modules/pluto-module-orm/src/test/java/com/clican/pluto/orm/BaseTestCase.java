/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm;

import java.io.File;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.dbunit.AbstractDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.jmock.Mockery;
import org.springframework.test.AbstractTransactionalSpringContextTests;

import com.clican.pluto.common.util.FileUtils;

public abstract class BaseTestCase extends
		AbstractTransactionalSpringContextTests {

	protected Log log = LogFactory.getLog(getClass());

	protected Mockery context = new Mockery();

	protected IDatabaseTester databaseTester;

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public BaseTestCase() {
		try {
			Velocity.init(Thread.currentThread().getContextClassLoader()
					.getResource("velocity.properties").getPath());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected String[] getConfigLocations() {
		if (!hasCachedContext("{classpath*:META-INF/orm-*.xml}")) {
			File dynamicMappingFolder = new File("dynamicMappingFolder/com");
			if (dynamicMappingFolder.exists()) {
				FileUtils.recurDelete(dynamicMappingFolder);
			}
		}
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:META-INF/orm-*.xml" };
	}

	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		try {
			databaseTester = new AbstractDatabaseTester() {
				public IDatabaseConnection getConnection() throws Exception {
					return new DatabaseConnection(dataSource.getConnection());
				}
			};
			// initialize your dataset here
			IDataSet dataSet = new XmlDataSet(getClass().getResourceAsStream(
					"/orm-test-data.xml"));
			databaseTester.setDataSet(dataSet);
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
}

// $Id: BaseTestCase.java 560 2009-06-16 05:33:11Z clican $