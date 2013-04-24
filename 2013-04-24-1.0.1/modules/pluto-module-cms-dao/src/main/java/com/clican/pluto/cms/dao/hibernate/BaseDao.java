/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.cms.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.clican.pluto.cms.dao.Dao;
import com.clican.pluto.orm.dynamic.inter.IPojo;

/**
 * This class implement the <code>Dao</code> interface via the 3rd-Party ORM
 * software-Hibernate.
 * 
 * It extends the <code>HibernateDaoSupport</code> class. It also means we can
 * use the <code>HibernateTemplate</code> to interact with the Hibernate
 * interface.
 * <p>
 * It also will provide some protected method used by subclass(e.g. get the
 * comma-split parameter string from <code>java.util.Collection<code> or array). 
 * <p>
 * As these common method shall be usually used, we implement them in the <code>BaseDao</code>
 * 
 * @since 0.0.1
 * @author clican
 * 
 */
public class BaseDao extends HibernateDaoSupport implements Dao {

	protected Log log = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	protected void initializeLazySet(Serializable obj, String propertyName) {
		try {
			Method method = obj.getClass().getMethod(
					"get" + propertyName.substring(0, 1).toUpperCase()
							+ propertyName.substring(1), new Class[] {});
			Set<Serializable> property = (Set<Serializable>) method.invoke(obj,
					new Object[] {});
			if (property == null) {
				return;
			}
			for (Serializable s : property) {
				if (s.getClass().getName().equals(obj.getClass().getName())) {
					initializeLazySet(s, propertyName);
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	protected HibernateTemplate createHibernateTemplate(
			SessionFactory sessionFactory) {
		HibernateTemplate hibernateTemplate = super
				.createHibernateTemplate(sessionFactory);
		hibernateTemplate.setCheckWriteOperations(false);
		return hibernateTemplate;
	}

	/**
	 * @see Dao#delete(Serializable)
	 */
	public void delete(Serializable object) {
		getHibernateTemplate().delete(object);
	}

	/**
	 * @see Dao#save(Serializable)
	 */
	public void save(Serializable object) {
		getHibernateTemplate().save(object);
	}

	/**
	 * @see Dao#update(Serializable)
	 */
	public void update(Serializable object) {
		getHibernateTemplate().update(object);
	}

	protected String getInString(Collection<? extends IPojo> collection) {
		StringBuffer str = new StringBuffer();
		str.append("(");
		for (IPojo pojo : collection) {
			str.append(pojo.getId());
			str.append(",");
		}
		return str.substring(0, str.length() - 1) + ")";
	}

	protected String getInString(IPojo[] array) {
		StringBuffer str = new StringBuffer();
		str.append("(");
		for (IPojo pojo : array) {
			str.append(pojo.getId());
			str.append(",");
		}
		return str.substring(0, str.length() - 1) + ")";
	}

}
// $Id: BaseDao.java 441 2009-06-05 09:04:20Z clican $