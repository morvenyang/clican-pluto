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
import java.util.Set;

import com.clican.pluto.common.dao.impl.CommonDaoSpringHibernateImpl;
import com.clican.pluto.fsm.dao.EventDao;
import com.clican.pluto.fsm.model.Event;
import com.clican.pluto.fsm.model.Variable;

public class EventDaoHibernateImpl extends CommonDaoSpringHibernateImpl
		implements EventDao {

	
	public void setVariable(Event event, String name, Serializable value) {
		Set<Variable> variableSet = event.getVariableSet();
		if (variableSet == null) {
			event.setVariableSet(new HashSet<Variable>());
		}

		boolean update = false;
		for (Variable org : event.getVariableSet()) {
			if (org.getName().equals(name)) {
				org.setValue(value);
				org.setChangeDate(new Date());
				update = true;
				break;
			}
		}
		if (!update) {
			Variable var = new Variable();
			var.setEvent(event);
			var.setName(name);
			var.setValue(value);
			var.setChangeDate(new Date());
			event.getVariableSet().add(var);
		}
		save(event);
	}

}

// $Id$