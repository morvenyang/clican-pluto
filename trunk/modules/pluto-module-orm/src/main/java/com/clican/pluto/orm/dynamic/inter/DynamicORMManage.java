/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.orm.dynamic.inter;

import com.clican.pluto.common.exception.ORMManageException;
import com.clican.pluto.orm.desc.ModelDescription;

/**
 * This interface defines all of dynamic object-relation mapping management
 * interface. Including create, update and delete.
 * 
 * @author clican
 * 
 */
public interface DynamicORMManage {

	/**
	 * Create a new object-relation mapping configuration. This configuration
	 * may use XML file or POJO annotation.
	 * 
	 * @param modelDescription
	 */
	public void generateORM(ModelDescription modelDescription)
			throws ORMManageException;

	/**
	 * Delete a existed object-relation mapping configuration.
	 * 
	 * @param modelDescription
	 * @throws ORMManageException
	 */
	public void deleteORM(ModelDescription modelDescription)
			throws ORMManageException;

	/**
	 * Update a existed object-relation mapping configuration.
	 * 
	 * @param modelDescription
	 * @throws ORMManageException
	 */
	public void updateORM(ModelDescription oldOne, ModelDescription newOne)
			throws ORMManageException;

}

// $Id: DynamicORMManage.java 580 2009-06-17 08:36:56Z clican $