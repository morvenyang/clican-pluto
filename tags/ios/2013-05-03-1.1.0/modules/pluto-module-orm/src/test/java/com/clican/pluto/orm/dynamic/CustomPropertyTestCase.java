/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dynamic;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.InputTextControl;
import com.clican.pluto.common.control.SelectOneControl;
import com.clican.pluto.common.type.CustomType;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.BaseTestCase;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.DynamicORMManage;
import com.clican.pluto.orm.dynamic.inter.IDataBaseOperation;
import com.clican.pluto.orm.dynamic.inter.SessionFactoryUpdate;

public class CustomPropertyTestCase extends BaseTestCase {

	private SessionFactoryUpdate sessionFactoryUpdate;

	private DynamicORMManage dynamicORMManage;

	private IDataBaseOperation dataBaseOperation;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactoryUpdate = ((SessionFactoryUpdate) sessionFactory);
	}

	public void setDynamicORMManage(DynamicORMManage dynamicORMManage) {
		this.dynamicORMManage = dynamicORMManage;
	}

	public void setDataBaseOperation(IDataBaseOperation dataBaseOperation) {
		this.dataBaseOperation = dataBaseOperation;
	}

	public void testAddCustomProperty() throws Exception {
		ModelDescription test1 = new ModelDescription("Test1", new String[] {
				"nickName", "password", "asd" }, new Type[] { new StringType(),
				new StringType(), new StringType(), new StringType() },
				new Control[] { new InputTextControl(), new InputTextControl(),
						new InputTextControl() });
		dynamicORMManage.generateORM(test1);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		SelectOneControl control = new SelectOneControl();
		control.setDynamic(true);
		ModelDescription test2 = new ModelDescription("Test2",
				new String[] { "test1" }, new Type[] { new CustomType(
						Constants.DYNAMIC_MODEL_PACKAGE + ".Test1") },
				new Control[] { control });
		dynamicORMManage.generateORM(test2);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
	}

	public void testRenameCustomProperty() throws Exception {
		ModelDescription test3 = new ModelDescription("Test3", new String[] {
				"nickName", "password", "asd" }, new Type[] { new StringType(),
				new StringType(), new StringType(), new StringType() },
				new Control[] { new InputTextControl(), new InputTextControl(),
						new InputTextControl() });
		dynamicORMManage.generateORM(test3);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		SelectOneControl control = new SelectOneControl();
		control.setDynamic(true);
		ModelDescription test4 = new ModelDescription("Test4",
				new String[] { "test3" }, new Type[] { new CustomType(
						Constants.DYNAMIC_MODEL_PACKAGE + ".Test3") },
				new Control[] { control });
		dynamicORMManage.generateORM(test4);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		ModelDescription test5 = test4.getCloneBean();
		test3.getPropertyDescriptionList().get(1).setName("test1List");
		dynamicORMManage.updateORM(test4, test5);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.alterTable(cfg, test4, test5);
	}

	public void testDropCustomProperty() throws Exception {
		ModelDescription test6 = new ModelDescription("Test6", new String[] {
				"nickName", "password", "asd" }, new Type[] { new StringType(),
				new StringType(), new StringType(), new StringType() },
				new Control[] { new InputTextControl(), new InputTextControl(),
						new InputTextControl() });
		dynamicORMManage.generateORM(test6);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		SelectOneControl control = new SelectOneControl();
		control.setDynamic(true);
		ModelDescription test7 = new ModelDescription("Test7",
				new String[] { "test6" }, new Type[] { new CustomType(
						Constants.DYNAMIC_MODEL_PACKAGE + ".Test6") },
				new Control[] { control });
		dynamicORMManage.generateORM(test7);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		ModelDescription test8 = test7.getCloneBean();
		test8.getPropertyDescriptionList().remove(1);
		dynamicORMManage.updateORM(test7, test8);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.alterTable(cfg, test7, test8);
	}
}

// $Id$