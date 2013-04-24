/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao.hibernate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.clican.pluto.common.dao.impl.CommonDaoSpringHibernateImpl;
import com.clican.pluto.fsm.dao.TaskDao;
import com.clican.pluto.fsm.model.State;
import com.clican.pluto.fsm.model.Task;
import com.clican.pluto.fsm.model.Variable;

public class TaskDaoHibernateImpl extends CommonDaoSpringHibernateImpl implements TaskDao {

	
	public Task findTaskById(Long taskId) {
		return (Task) this.getHibernateTemplate().get(Task.class, taskId);
	}

	@SuppressWarnings("unchecked")
	
	public List<Task> findActiveTasksByState(State state) {
		StringBuffer hql = new StringBuffer("from Task where state.id = :stateId and completeTime is null");
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("stateId", state.getId());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	
	public List<Task> findTasksByParams(String userId, String sessionName, String stateName, boolean completed) {
		StringBuffer hql = new StringBuffer("from Task where");
		if (completed) {
			hql.append(" completeTime is not null");
		} else {
			hql.append(" completeTime is null");
		}
		if (StringUtils.isNotEmpty(sessionName)) {
			hql.append(" and state.session.name = :sessionName");
		}
		if (StringUtils.isNotEmpty(userId)) {
			hql.append(" and assignee= :userId");
		}
		if (StringUtils.isNotEmpty(stateName)) {
			hql.append(" and state.name = :stateName");
		}
		Query query = this.getSession().createQuery(hql.toString());
		if (StringUtils.isNotEmpty(sessionName)) {
			query.setParameter("sessionName", sessionName);
		}
		if (StringUtils.isNotEmpty(userId)) {
			query.setParameter("userId", userId);
		}
		if (StringUtils.isNotEmpty(stateName)) {
			query.setParameter("stateName", stateName);
		}
		return query.list();
	}

	
	public void setVariable(Task task, String name, Serializable value) {
		Set<Variable> variableSet = task.getVariableSet();
		if (variableSet == null) {
			task.setVariableSet(new HashSet<Variable>());
		}

		boolean update = false;
		for (Variable org : task.getVariableSet()) {
			if (org.getName().equals(name)) {
				org.setValue(value);
				org.setChangeDate(new Date());
				update = true;
				break;
			}
		}
		if (!update) {
			Variable var = new Variable();
			var.setTask(task);
			var.setName(name);
			var.setValue(value);
			var.setChangeDate(new Date());
			task.getVariableSet().add(var);
		}
		save(task);
	}

	@SuppressWarnings("unchecked")
	
	public void completeTasksBySessionId(Long sessionId) {
		Calendar current = Calendar.getInstance();
		Query query = this.getSession().createQuery(
				"from Task where completeTime is null and state.session.id = :sessionId");
		query.setParameter("sessionId", sessionId);
		List<Task> tasks = query.list();
		for (Task task : tasks) {
			task.setCompleteTime(current.getTime());
			save(task);
		}
	}

	@SuppressWarnings("unchecked")
	
	public void completeTasksByStateId(Long stateId) {
		Calendar current = Calendar.getInstance();
		Query query = this.getSession().createQuery("from Task where completeTime is null and state.id = :stateId");
		query.setParameter("stateId", stateId);
		List<Task> tasks = query.list();
		for (Task task : tasks) {
			task.setCompleteTime(current.getTime());
			save(task);
		}
	}

}

// $Id$