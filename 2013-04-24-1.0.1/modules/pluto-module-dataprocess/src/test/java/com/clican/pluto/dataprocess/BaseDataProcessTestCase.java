/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.dataprocess;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class BaseDataProcessTestCase extends AbstractDependencyInjectionSpringContextTests {

	protected IDatabaseTester databaseTester;

	protected Log log = LogFactory.getLog(getClass());

	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:META-INF/spring-dp*.xml" };
	}

	
	protected void onSetUp() throws Exception {
		super.onSetUp();
		Connection conn = null;
		Statement stat = null;
		InputStream is = null;
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa", "");
			stat = conn.createStatement();

			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ibatis/createtable-dataprocess-test.sql");
			byte[] script = new byte[is.available()];
			is.read(script);

			String sql = new String(script);
			stat.execute(sql);

			databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:.", "sa", "");

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
		} finally {
			if (stat != null) {
				stat.close();
			}
			if (conn != null) {
				conn.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}

	
	protected void onTearDown() throws Exception {
		super.onTearDown();
		try {
			databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
			databaseTester.onTearDown();
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

	protected IDataSet getDataSet() throws Exception {
		IDataSet data = new XmlDataSet(getClass().getResourceAsStream("/data/default.xml"));
		return data;
	}
}

// $Id$