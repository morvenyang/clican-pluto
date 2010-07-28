/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cms.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;

import com.clican.pluto.cms.core.BaseTestCase;
import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.control.Control;
import com.clican.pluto.common.control.SelectMutilControl;
import com.clican.pluto.common.control.SelectOneControl;
import com.clican.pluto.common.type.CustomListType;
import com.clican.pluto.common.type.CustomType;
import com.clican.pluto.common.type.Type;
import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.ModelContainer;

public class DataStructureServiceTestCase extends BaseTestCase {

	private DataStructureService dataStructureService;

	private ModelContainer modelContainer;

	public void setDataStructureService(
			DataStructureService dataStructureService) {
		this.dataStructureService = dataStructureService;
		modelContainer = this.context.mock(ModelContainer.class);
		this.dataStructureService.setModelContainer(modelContainer);
	}

	public void testInit() {
		Constants.DATA_STRUCTURE_XHTML_FOLDER = new File("target");
		context.checking(new Expectations() {
			{
				one(modelContainer).getModelDescs();
				List<ModelDescription> result = new ArrayList<ModelDescription>();
				ModelDescription md1 = new ModelDescription("Md1",
						new String[] {});
				ModelDescription md2 = new ModelDescription("Md2",
						new String[] {});
				Control control1 = new SelectOneControl();
				control1.setDynamic(true);
				Control control2 = new SelectMutilControl();
				control2.setDynamic(true);
				ModelDescription md3 = new ModelDescription("Md3",
						new String[] { "md1", "md2List" }, new Type[] {
								new CustomType(Constants.DYNAMIC_MODEL_PACKAGE
										+ ".Md1"),
								new CustomListType(new CustomType(
										Constants.DYNAMIC_MODEL_PACKAGE
												+ ".Md2")) }, new Control[] {
								control1, control2 });
				result.add(md1);
				result.add(md2);
				result.add(md3);
				will(returnValue(result));
			}
		});
		this.dataStructureService.init();
		for (File file : Constants.DATA_STRUCTURE_XHTML_FOLDER.listFiles()) {
			file.delete();
		}
		Constants.DATA_STRUCTURE_XHTML_FOLDER.delete();
	}
}

// $Id$