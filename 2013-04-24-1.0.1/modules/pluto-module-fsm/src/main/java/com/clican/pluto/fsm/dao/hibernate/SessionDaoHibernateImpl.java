/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.clican.pluto.common.dao.impl.CommonDaoSpringHibernateImpl;
import com.clican.pluto.fsm.dao.SessionDao;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.Variable;

public class SessionDaoHibernateImpl extends CommonDaoSpringHibernateImpl
		implements SessionDao {

	
	public Session findSessionById(Long sessionId) {
		return (Session) this.getHibernateTemplate().get(Session.class,
				sessionId);
	}

	
	public void setVariable(Session session, String name, Serializable value) {
		Set<Variable> variableSet = session.getVariableSet();
		if (variableSet == null) {
			session.setVariableSet(new HashSet<Variable>());
		}

		boolean update = false;
		for (Variable org : session.getVariableSet()) {
			if (org.getName().equals(name)) {
				org.setValue(value);
				org.setChangeDate(new Date());
				update = true;
				break;
			}
		}
		if (!update) {
			Variable var = new Variable();
			var.setSession(session);
			var.setName(name);
			var.setValue(value);
			var.setChangeDate(new Date());
			session.getVariableSet().add(var);
		}
		save(session);
	}

	@SuppressWarnings("unchecked")
	
	public Serializable getVariableValue(Long sessionId, String variableName) {
		Query query = this
				.getSession()
				.createQuery(
						"from Variable var where var.session.id = :sessionId and name = :variableName");
		query.setParameter("sessionId", sessionId);
		query.setParameter("variableName", variableName);
		List<Variable> list = query.list();
		if (list.size() > 0) {
			return list.get(0).getValue();
		}
		return null;
	}

}

// $Id$