/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao;

import java.io.Serializable;
import java.util.List;

import com.clican.pluto.orm.desc.ModelDescription;
import com.clican.pluto.orm.dynamic.inter.IDataModel;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

/**
 * This interface defines data model operation.
 * <p>
 * The data model is used by content management system, it can define all kinds
 * of data object. These data object may be mapped to a database table or a LDAP
 * object class. And these data model's instance may be a record in table or an
 * entry in LDAP.
 * <P>
 * This interface includes following operations:
 * <ul>
 * <li>Create an new data model via end user selection by defining various
 * attribute and attribute constraints including whether these attribute can be
 * null or the length of attribute.</li>
 * <li>Update an existed data model, it may add attributes or remove attributes
 * or alter the attribute constraints. This operation also will cause some of
 * property will be deleted from created instance.</li>
 * <li>Delete an existed data model. This operation also will cause all of this
 * data model's record will also be removed.</li>
 * </ul>
 * <p>
 * Some of constraints may be supported by Database and others may be supported
 * by LDAP. But I think most of constraints shall be supported by both database
 * and LDAP.
 * 
 * @since 0.0.1
 * @author clican
 * 
 */
public interface DataModelDao extends Dao {

	public List<Serializable> query(String queryString);

	public void delete(List<IDataModel> dataModels,
			ModelDescription modelDescription);

	public List<IDataModel> getDataModels(IDirectory directory,
			ModelDescription modelDescription, List<String> orderBy);

	public List<IDataModel> getDataModels(ModelDescription modelDescription,
			String name);

	public IDataModel loadDataModels(Class<?> clazz, Long id);

}
// $Id: DataModelDao.java 556 2009-06-16 04:30:01Z clican $