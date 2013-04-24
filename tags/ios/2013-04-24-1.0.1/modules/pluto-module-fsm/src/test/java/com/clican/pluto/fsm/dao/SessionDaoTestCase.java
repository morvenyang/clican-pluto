/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao;

import java.util.Set;

import com.clican.pluto.fsm.dao.SessionDao;
import com.clican.pluto.fsm.model.Session;
import com.clican.pluto.fsm.model.Variable;

public class SessionDaoTestCase extends BaseDaoTestCase {

	private SessionDao sessionDao;

	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	public void testSetVariable() throws Exception {
		Session session = sessionDao.findSessionById(1L);
		sessionDao.setVariable(session, "test", "bbb");
		this.setComplete();
		this.endTransaction();
		int row = this.databaseTester.getConnection().getRowCount("FSM_VARIABLE");
		assertEquals(1, row);
		this.startNewTransaction();
		session = sessionDao.findSessionById(1L);
		Set<Variable> varSet = session.getVariableSet();
		assertEquals(1, varSet.size());
		Variable var = varSet.iterator().next();
		assertEquals("test", var.getName());
		assertEquals("bbb", var.getValue());
	}

	public void testCascadeDeleteSession() throws Exception {
		Session session = sessionDao.findSessionById(1L);
		sessionDao.delete(session);
	}
}

// $Id$