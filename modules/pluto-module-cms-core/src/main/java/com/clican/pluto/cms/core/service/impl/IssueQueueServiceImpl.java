/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.cms.core.service.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.clican.pluto.cms.core.service.IssueQueueService;
import com.clican.pluto.cms.dao.IssueQueueDao;
import com.clican.pluto.orm.model.IssueQueue;

public class IssueQueueServiceImpl implements IssueQueueService {

	private final static Log log = LogFactory
			.getLog(IssueQueueServiceImpl.class);

	private ThreadPoolExecutor sendExecutor;

	private LinkedBlockingQueue<Runnable> issueQueue = new LinkedBlockingQueue<Runnable>();

	private IssueQueueDao issueQueueDao;

	private int threadNumber = 10;

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	public void setIssueQueueDao(IssueQueueDao issueQueueDao) {
		this.issueQueueDao = issueQueueDao;
	}

	public void start() {
		log.info("starting issue queue pool sender ..." + this);

		int coreNumber = 3;
		if (coreNumber > threadNumber) {
			coreNumber = threadNumber;
		}
		sendExecutor = new ThreadPoolExecutor(coreNumber, threadNumber, 60,
				TimeUnit.SECONDS, issueQueue);
	}

	@Transactional
	public void issueImmediately(List<IssueQueue> queueList) {
		issueSchedule(queueList);
	}

	@Transactional
	public void issueSchedule(List<IssueQueue> queueList) {
		for (IssueQueue queue : queueList) {
			issueQueueDao.save(queue);
		}
	}

	/**
	 * There is no transaction on this method
	 */
	public void onCheckIssueQueue() {

	}

}

// $Id$