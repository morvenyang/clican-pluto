/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.dao.impl;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.clican.pluto.common.dao.CommonDao;

public class CommonDaoSpringHibernateImpl extends HibernateDaoSupport implements
		CommonDao {

	protected final Log log = LogFactory.getLog(getClass());

	public void delete(Object model) {
		this.getHibernateTemplate().delete(model);
	}

	public Serializable save(Object model) {
		return this.getHibernateTemplate().save(model);
	}

	public void update(Object model) {
		this.getHibernateTemplate().update(model);
	}

	public void merge(Object model) {
		this.getHibernateTemplate().merge(model);
	}

	/**
	 * 产生 select、delete、update 时候where 语句中in部分的String
	 * 
	 * @param <X>
	 * @param ids
	 *            数组类型的 值列表
	 * @param addSemi
	 *            是否单引号，
	 * @return
	 */
	protected String getInString(Object[] ids, boolean addSemi) {
		if (ids == null || ids.length == 0) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" in (");
		for (Object id : ids) {
			if (addSemi) {
				sql.append("'" + id.toString() + "',");
			} else {
				sql.append(id.toString() + ",");
			}
		}
		String result = sql.toString();
		result = result.substring(0, result.length() - 1) + ")";
		return result;
	}

	/**
	 * 产生 select、delete、update 时候where 语句中in部分的String
	 * 
	 * @param <X>
	 * @param ids
	 *            collection类型的 值列表
	 * @param addSemi
	 *            是否单引号，
	 * @return
	 */
	protected <X extends Collection<?>> String getInString(X ids,
			boolean addSemi) {
		if (ids == null || ids.size() == 0) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" in (");
		for (Object id : ids) {
			if (id == null)
				continue;
			if (addSemi) {
				sql.append("'" + id.toString() + "',");
			} else {
				sql.append(id.toString() + ",");
			}
		}
		String result = sql.toString();
		result = result.substring(0, result.length() - 1) + ")";
		return result;
	}

}

// $Id$