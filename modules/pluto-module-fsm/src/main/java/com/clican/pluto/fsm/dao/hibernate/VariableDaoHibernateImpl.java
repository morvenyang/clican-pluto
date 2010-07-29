/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.clican.pluto.common.dao.impl.CommonDaoSpringHibernateImpl;
import com.clican.pluto.fsm.dao.VariableDao;
import com.clican.pluto.fsm.model.Variable;

public class VariableDaoHibernateImpl extends CommonDaoSpringHibernateImpl implements VariableDao {

	@SuppressWarnings("unchecked")
	
	public Map<String, Serializable> getSessionVariableValues(Long sessionId) {
		Query query = this.getSession().createQuery("from Variable var where var.session.id = :sessionId");
		query.setParameter("sessionId", sessionId);
		List<Variable> list = query.list();
		Map<String, Serializable> variables = new HashMap<String, Serializable>();
		for (Variable var : list) {
			variables.put(var.getName(), var.getValue());
		}

		return variables;
	}

}

// $Id$