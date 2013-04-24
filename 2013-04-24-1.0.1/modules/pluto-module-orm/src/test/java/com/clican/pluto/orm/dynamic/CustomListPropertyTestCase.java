/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.dynamic;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.InputTextControl;
import com.clican.pluto.common.control.SelectMutilControl;
import com.clican.pluto.common.type.CustomListType;
import com.clican.pluto.common.type.CustomType;
import com.clican.pluto.common.type.StringType;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.BaseTestCase;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.DynamicORMManage;
import com.clican.pluto.orm.dynamic.inter.IDataBaseOperation;
import com.clican.pluto.orm.dynamic.inter.SessionFactoryUpdate;

public class CustomListPropertyTestCase extends BaseTestCase {

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

	public void testAddCustomListProperty() throws Exception {
		ModelDescription test1 = new ModelDescription(
				"TestAddCustomListProperty1", new String[] { "nickName",
						"password", "asd" }, new Type[] { new StringType(),
						new StringType(), new StringType(), new StringType() },
				new Control[] { new InputTextControl(), new InputTextControl(),
						new InputTextControl() });
		dynamicORMManage.generateORM(test1);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		SelectMutilControl control = new SelectMutilControl();
		control.setDynamic(true);
		ModelDescription test2 = new ModelDescription(
				"TestAddCustomListProperty2", new String[] { "test1" },
				new Type[] { new CustomListType(new CustomType(
						Constants.DYNAMIC_MODEL_PACKAGE
								+ ".TestAddCustomListProperty1")) },
				new Control[] { control });
		dynamicORMManage.generateORM(test2);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
	}

	public void renameAddCustomListProperty() throws Exception {
		ModelDescription test1 = new ModelDescription(
				"TestRenameCustomListProperty1", new String[] { "nickName",
						"password", "asd" }, new Type[] { new StringType(),
						new StringType(), new StringType(), new StringType() },
				new Control[] { new InputTextControl(), new InputTextControl(),
						new InputTextControl() });
		dynamicORMManage.generateORM(test1);
		Configuration cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		SelectMutilControl control = new SelectMutilControl();
		control.setDynamic(true);
		ModelDescription test2 = new ModelDescription(
				"TestRenameCustomListProperty2", new String[] { "test1" },
				new Type[] { new CustomListType(new CustomType(
						Constants.DYNAMIC_MODEL_PACKAGE
								+ ".TestRenameCustomListProperty1")) },
				new Control[] { control });
		dynamicORMManage.generateORM(test2);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.createTable(cfg);
		ModelDescription test3 = test2.getCloneBean();
		test3.getPropertyDescriptionList().get(1).setName("test1List");
		dynamicORMManage.updateORM(test2, test3);
		cfg = sessionFactoryUpdate.update();
		dataBaseOperation.alterTable(cfg, test2, test3);
	}

	public void dropAddCustomListProperty() throws Exception {

	}
}

// $Id$