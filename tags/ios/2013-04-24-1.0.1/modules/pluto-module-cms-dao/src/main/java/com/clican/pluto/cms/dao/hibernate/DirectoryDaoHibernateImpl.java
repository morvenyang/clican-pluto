/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao.hibernate;

import java.lang.reflect.Method;
import java.util.List;

import com.clican.pluto.cms.dao.DirectoryDao;
import com.clican.pluto.common.constant.Constants;
import com.clican.pluto.common.exception.PlutoException;
import com.clican.pluto.common.util.ClassUtils;
import com.clican.pluto.common.util.StringUtils;
import com.clican.pluto.orm.annotation.DynamicModelSet;
import com.clican.pluto.orm.dynamic.inter.IDirectory;

public class DirectoryDaoHibernateImpl extends BaseDao implements DirectoryDao {

	private ClassLoader dynamicClassLoader;

	public void setDynamicClassLoader(ClassLoader dynamicClassLoader) {
		this.dynamicClassLoader = dynamicClassLoader;
	}

	@SuppressWarnings("unchecked")
	public IDirectory load(final String path) {
		List<IDirectory> result = this.getHibernateTemplate().findByNamedParam(
				"from Directory d where d.path = :path", "path", path);
		if (result.size() != 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public IDirectory load(Long id) {
		return (IDirectory) getHibernateTemplate()
				.load(getDirectoryClass(), id);
	}

	public Object[] getDirectoryModelCount(Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		Class<?> clazz = this.getDirectoryClass();
		List<Method> methods = ClassUtils.getMethods(clazz,
				DynamicModelSet.class);
		for (int i = 0; i < methods.size(); i++) {
			Method method = methods.get(i);
			String propertyName = StringUtils
					.getPropertyNameByGetMethodName(method.getName());
			sql.append("'"
					+ method.getAnnotation(DynamicModelSet.class).modelName()
					+ "',");
			if (i == methods.size() - 1) {
				sql.append("d." + propertyName + ".size");
			} else {
				sql.append("d." + propertyName + ".size,");
			}
		}
		sql.append(" from Directory d where d.id = :id");
		return (Object[]) getHibernateTemplate().findByNamedParam(
				sql.toString(), "id", id).get(0);
	}

	private Class<?> getDirectoryClass() {
		Class<?> clazz = null;
		try {
			clazz = dynamicClassLoader
					.loadClass(Constants.DYNAMIC_MODEL_PACKAGE + "."
							+ Constants.DEFAULT_DIRECTORY_CLASS_NAME);
		} catch (Exception e) {
			throw new PlutoException(e);
		}
		return clazz;
	}
}

// $Id$