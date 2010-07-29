/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.fsm.engine.impl;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.fsm.dao.JobDao;
import com.clican.pluto.fsm.engine.JobService;
import com.clican.pluto.fsm.enumeration.JobStatus;
import com.clican.pluto.fsm.model.Job;
import com.clican.pluto.fsm.model.State;

/**
 * 和Job相关的主要处理Job增删改查的接口
 * 
 * @author wei.zhang
 * 
 */
public class JobServiceImpl implements JobService {

	private JobDao jobDao;

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	@Transactional
	
	public void deleteJob(Job job) {
		jobDao.delete(job);
	}

	@Transactional
	
	public void saveJob(Job job) {
		jobDao.save(job);
	}

	@Transactional
	
	public void updateJob(Job job) {
		jobDao.update(job);
	}

	@Transactional
	
	public List<Job> findJobByExecuteTime(Date executeTime, JobStatus status) {
		return jobDao.findJobByExecuteTime(executeTime,status);
	}

	@Transactional
	
	public Job findJobById(Long jobId) {
		return jobDao.findJobById(jobId);
	}

	@Transactional
	
	public List<Job> findIdelJobsByState(State state) {
		return jobDao.findIdelJobsByState(state);
	}

}

// $Id$