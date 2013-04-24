/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.constant;

import java.io.File;

import org.springframework.context.ApplicationContext;

public class Constants {

	public final static String DYNAMIC_MODEL_PACKAGE = "com.clican.pluto.orm.model.dynamic";

	public final static String DYNAMIC_MODEL_PACKAGE_PATH = "com/clican/pluto/orm/model/dynamic";
	
	public final static String DEFAULT_DIRECTORY_CLASS_NAME = "Directory";
	
	public final static String DEFAULT_TEMPLATE_CLASS_NAME = "Template";
	
	public final static String DEFAULT_SITE_CLASS_NAME = "Site";
	
	public static ApplicationContext ctx = null;
	
	public static File DATA_STRUCTURE_XHTML_FOLDER = null;

}

// $Id$