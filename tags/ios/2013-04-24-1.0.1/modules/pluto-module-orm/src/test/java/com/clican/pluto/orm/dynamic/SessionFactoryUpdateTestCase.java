/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.dynamic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.clican.pluto.common.control.CalendarControl;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.InputTextControl;
import com.clican.pluto.common.type.CalendarType;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.BaseTestCase;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.desc.PropertyDescription;
import com.clican.pluto.orm.dynamic.impl.DynamicORMManagePojoHibernateImpl;
import com.clican.pluto.orm.dynamic.inter.DynamicORMManage;
import com.clican.pluto.orm.dynamic.inter.IDataBaseOperation;
import com.clican.pluto.orm.dynamic.inter.SessionFactoryUpdate;

public class SessionFactoryUpdateTestCase extends BaseTestCase {

	private SessionFactoryUpdate sessionFactoryUpdate;

	private SessionFactory sessionFactory;

	private DynamicORMManage dynamicORMManage;

	private IDataBaseOperation dataBaseOperation;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.sessionFactoryUpdate = ((SessionFactoryUpdate) sessionFactory);
	}

	public void setDynamicORMManage(DynamicORMManage dynamicORMManage) {
		this.dynamicORMManage = dynamicORMManage;
	}

	public void setDataBaseOperation(IDataBaseOperation dataBaseOperation) {
		this.dataBaseOperation = dataBaseOperation;
	}

	public void testUpdate() throws Exception {
		sessionFactoryUpdate.update();
	}

	public void testUpdateAfterCreateDynamicModel() throws Exception {
		ModelDescription modelDescription = new ModelDescription("Test",
				new String[] { "nickName", "password", "asd" }, new Type[] {
						new StringType(), new StringType(), new StringType(),
						new StringType() }, new Control[] {
						new InputTextControl(), new InputTextControl(),
						new InputTextControl() });
		dynamicORMManage.generateORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		Session session = sessionFactory.openSession();
		String sql;
		if (dynamicORMManage instanceof DynamicORMManagePojoHibernateImpl) {
			sql = "from Test t where t.nickName='asd'";
		} else {
			sql = "from Test t where t.userAttributes.nickName='asd'";
		}
		Query q1 = session.createQuery(sql);
		int size = q1.list().size();
		assertEquals(0, size);
	}

	public void testUpdateAfterUpdateDynamicModel() throws Exception {
		ModelDescription modelDescription = new ModelDescription("Test",
				new String[] { "nickName", "password" },
				new Type[] { new StringType(), new StringType(),
						new CalendarType() }, new Control[] {
						new InputTextControl(), new InputTextControl(),
						new CalendarControl() });
		dynamicORMManage.generateORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		ModelDescription oldOne = modelDescription;
		ModelDescription newOne = modelDescription.getCloneBean();
		PropertyDescription pd = new PropertyDescription();
		pd.setName("nickName2");
		pd.setType(new StringType());
		newOne.getPropertyDescriptionList().add(pd);
		dynamicORMManage.updateORM(oldOne,newOne);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.alterTable(cfg, oldOne, newOne);
		String sql;
		if (dynamicORMManage instanceof DynamicORMManagePojoHibernateImpl) {
			sql = "from Test t where t.nickName2='asd'";
		} else {
			sql = "from Test t where t.userAttributes.nickName2='asd'";
		}
		Session session = sessionFactory.openSession();
		Query q1 = session.createQuery(sql);
		int size = q1.list().size();
		assertEquals(0, size);
	}

	public void testUpdateAfterDeleteDynamicModel() throws Exception {
		ModelDescription modelDescription = new ModelDescription("Test",
				new String[] { "nickName", "password" }, new Type[] {
						new StringType(), new StringType() }, new Control[] {
						new InputTextControl(), new InputTextControl() });
		dynamicORMManage.generateORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		dynamicORMManage.deleteORM(modelDescription);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.dropTable(modelDescription);
	}

	public void testRenameColumn() throws Exception {
		ModelDescription modelDescription = new ModelDescription("Test",
				new String[] { "nickName", "password" }, new Type[] {
						new StringType(), new StringType() }, new Control[] {
						new InputTextControl(), new InputTextControl() });
		dynamicORMManage.generateORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		ModelDescription oldOne = modelDescription;
		ModelDescription newOne = modelDescription.getCloneBean();
		PropertyDescription pd = newOne.getPropertyDescriptionList().get(2);
		pd.setName("newpassword");
		dynamicORMManage.updateORM(oldOne,newOne);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.alterTable(cfg, oldOne, newOne);
		String sql = "from Test t where t.newpassword = 'asd'";
		Session session = sessionFactory.openSession();
		Query q1 = session.createQuery(sql);
		int size = q1.list().size();
		assertEquals(0, size);
	}

}

// $Id: SessionFactoryUpdateTestCase.java 580 2009-06-17 08:36:56Z clican $