/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author clican
 *
 */
package com.clican.pluto.cms.dao;

import java.io.Serializable;

/**
 * This interface defines basic data operation, including create, update, delete
 * data. The implementation may operator detail data base on Database or LDAP.
 * 
 * For different database interaction, it is processed by their JDBC driver. And
 * we also introduce the ORM software-Hibernate to warp the standard JDBC
 * operation.
 * 
 * @since 0.0.1
 * @author clican
 * 
 */
public interface Dao {

	/**
	 * Store the object on some data storage device. It may be a database, flat
	 * file or a LDAP server. The exact storage device shall be configured by
	 * Spring configuration file.
	 * 
	 * @param object
	 *            the serializable object which shall be saved to someone data
	 *            storage.
	 */
	public void save(Serializable object);

	/**
	 * Update the object which has been stored on the data storage device.
	 * 
	 * @param object
	 *            the serializable object which shall be updated.
	 */
	public void update(Serializable object);

	/**
	 * Delete or remove the object which has been stored on the data storage
	 * device.
	 * 
	 * @param object
	 *            the serializable object which shall be deleted or removed.
	 */
	public void delete(Serializable object);

}
// $Id: Dao.java 425 2009-06-05 03:44:30Z clican $