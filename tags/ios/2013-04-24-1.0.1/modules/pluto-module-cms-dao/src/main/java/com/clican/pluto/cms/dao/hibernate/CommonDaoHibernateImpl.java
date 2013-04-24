/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao.hibernate;

import java.util.Collection;

import javax.persistence.Entity;

import com.clican.pluto.cms.dao.CommonDao;
import com.clican.pluto.orm.dynamic.inter.IPojo;

public class CommonDaoHibernateImpl extends BaseDao implements CommonDao {

	public IPojo reloadPojo(IPojo source) {
		return (IPojo) getHibernateTemplate().load(source.getClass(),
				source.getId());
	}

	@SuppressWarnings("unchecked")
	public Collection<IPojo> reloadPojos(Collection<IPojo> source) {
		if (source == null || source.size() == 0) {
			return source;
		}
		IPojo pojo = source.toArray(new IPojo[] {})[0];
		StringBuffer sql = new StringBuffer();
		sql.append("from ");
		sql.append(pojo.getClass().getAnnotation(Entity.class).name());
		sql.append(" where id in ");
		sql.append(getInString(source));
		return getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public IPojo[] reloadPojos(IPojo[] source) {
		if (source == null || source.length == 0) {
			return source;
		}
		IPojo pojo = source[0];
		StringBuffer sql = new StringBuffer();
		sql.append("from ");
		sql.append(pojo.getClass().getAnnotation(Entity.class).name());
		sql.append(" where id in ");
		sql.append(getInString(source));
		return (IPojo[]) getHibernateTemplate().find(sql.toString()).toArray(
				new IPojo[] {});
	}

}

// $Id$