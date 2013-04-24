/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.InputTextControl;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.BaseTestCase;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.DynamicORMManage;
import com.clican.pluto.orm.dynamic.inter.IDataBaseOperation;
import com.clican.pluto.orm.dynamic.inter.SessionFactoryUpdate;

public class SessionFactoryUpdateInChineseTestCase extends BaseTestCase {

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
		ModelDescription modelDescription = new ModelDescription("测试",
				new String[] { "test", "nickName", "password", "属性" },
				new Type[] { new StringType(), new StringType(),
						new StringType(), new StringType(), new StringType() },
				new Control[] { new InputTextControl(), new InputTextControl(),
						new InputTextControl(), new InputTextControl() });
		dynamicORMManage.generateORM(modelDescription);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		Session session = sessionFactory.openSession();
		String sql = "from 测试 t where t.属性='asd'";
		Query q1 = session.createQuery(sql);
		int size = q1.list().size();
		assertEquals(0, size);
	}

}

// $Id$