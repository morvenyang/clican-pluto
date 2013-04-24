/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author weizha
 *
 */
package com.clican.pluto.cms.core.service;

import java.util.List;

import com.clican.pluto.orm.model.IssueQueue;

public interface IssueQueueService {

	/**
	 * 添加到发布队列, 立即发布
	 * 
	 * @param queueList
	 */
	public void issueImmediately(List<IssueQueue> queueList);

	public void issueSchedule(List<IssueQueue> queueList);

	public void onCheckIssueQueue();
}

// $Id$