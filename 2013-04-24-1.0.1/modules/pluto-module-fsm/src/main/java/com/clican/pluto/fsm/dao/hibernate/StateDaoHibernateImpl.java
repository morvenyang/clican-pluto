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
import com.clican.pluto.fsm.dao.StateDao;
import com.clican.pluto.fsm.enumeration.Status;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Variable;

public class StateDaoHibernateImpl extends CommonDaoSpringHibernateImpl
		implements StateDao {

	
	public void setVariable(State state, String name, Serializable value) {
		Set<Variable> variableSet = state.getVariableSet();
		if (variableSet == null) {
			state.setVariableSet(new HashSet<Variable>());
		}

		boolean update = false;
		for (Variable org : state.getVariableSet()) {
			if (org.getName().equals(name)) {
				org.setValue(value);
				org.setChangeDate(new Date());
				update = true;
				break;
			}
		}
		if (!update) {
			Variable var = new Variable();
			var.setState(state);
			var.setName(name);
			var.setValue(value);
			var.setChangeDate(new Date());
			state.getVariableSet().add(var);
		}
		save(state);
	}

	@SuppressWarnings("unchecked")
	
	public List<State> getActiveStates(Long sessionId) {
		Query query = this
				.getSession()
				.createQuery(
						"from State where session.id = :sessionId and status = :status");
		query.setParameter("sessionId", sessionId);
		query.setParameter("status", Status.ACTIVE.getStatus());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	
	public List<State> getActiveAndPendingState(Long sessionId) {
		Query query = this
				.getSession()
				.createQuery(
						"from State where session.id = :sessionId and status != :status");
		query.setParameter("sessionId", sessionId);
		query.setParameter("status", Status.INACTIVE.getStatus());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	
	public State getPendingState(Long sessionId, String stateName) {
		Query query = this
				.getSession()
				.createQuery(
						"from State where session.id = :sessionId and status = :status and name = :stateName");
		query.setParameter("sessionId", sessionId);
		query.setParameter("status", Status.PENDING.getStatus());
		query.setParameter("stateName", stateName);
		List<State> states = query.list();
		if (states.size() == 1) {
			return states.get(0);
		} else if (states.size() == 0) {
			return null;
		} else {
			throw new RuntimeException("The pending state shall only be one");
		}
	}

	
	public State findStateById(Long stateId) {
		return (State) this.getSession().get(State.class, stateId);
	}

	
	public void inactiveStatesBySessionId(Long sessionId) {
		Query query = this
				.getSession()
				.createQuery(
						"update State set status = :status where session.id = :sessionId and status != :status");
		query.setParameter("status", Status.INACTIVE.getStatus());
		query.setParameter("sessionId", sessionId);
		int row = query.executeUpdate();
		if (log.isDebugEnabled()) {
			log.debug("update " + row + " rows state to inactive");
		}
	}

	
	public void inactiveState(Long sessionId, String[] stateNames) {
		Query query = this
				.getSession()
				.createQuery(
						"update State set status = :status where session.id = :sessionId and status != :status and name"
								+ getInString(stateNames, true));
		query.setParameter("status", Status.INACTIVE.getStatus());
		query.setParameter("sessionId", sessionId);
		int row = query.executeUpdate();
		if (log.isDebugEnabled()) {
			log.debug("update " + row + " rows state to inactive");
		}

	}

}

// $Id$