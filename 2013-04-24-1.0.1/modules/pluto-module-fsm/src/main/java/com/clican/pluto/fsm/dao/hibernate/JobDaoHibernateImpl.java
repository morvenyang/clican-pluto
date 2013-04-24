/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.clican.pluto.common.dao.impl.CommonDaoSpringHibernateImpl;
import com.clican.pluto.fsm.dao.JobDao;
import com.clican.pluto.fsm.enumeration.JobStatus;
import com.clican.pluto.fsm.model.Job;
import com.clican.pluto.fsm.model.State;

public class JobDaoHibernateImpl extends CommonDaoSpringHibernateImpl implements
		JobDao {

	@SuppressWarnings("unchecked")
	public List<Job> findJobByExecuteTime(Date executeTime, JobStatus status) {
		Query query = getSession()
				.createQuery(
						"from Job where status = :status and executeTime <= :executeTime");
		query.setParameter("executeTime", executeTime);
		query.setParameter("status", status.getStatus());
		return query.list();
	}

	public Job findJobById(Long jobId) {
		return (Job) this.getHibernateTemplate().get(Job.class, jobId);
	}

	@SuppressWarnings("unchecked")
	public List<Job> findIdelJobsByState(State state) {
		Query query = this.getSession().createQuery(
				"from Job where state.id = :stateId and status = :status");
		query.setParameter("stateId", state.getId());
		query.setParameter("status", JobStatus.IDLE.getStatus());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public void deleteJobsBySessionId(final Long sessionId) {
		Query query = this.getSession().createQuery(
				"from Job where state.session.id = :sessionId");
		query.setParameter("sessionId", sessionId);
		List<Job> jobList = query.list();
		for (Job job : jobList) {
			delete(job);
		}
		if (log.isDebugEnabled()) {
			log.debug("delete " + jobList.size() + " row jobs sessionId["
					+ sessionId + "]");
		}
	}

	public void deleteJobsByStateId(Long stateId) {
		Query query = this.getSession().createQuery(
				"delete from Job where state.id = :stateId");
		query.setParameter("stateId", stateId);
		int row = query.executeUpdate();
		if (log.isDebugEnabled()) {
			log.debug("delete " + row + " row jobs stateId[" + stateId + "]");
		}
	}

}

// $Id$