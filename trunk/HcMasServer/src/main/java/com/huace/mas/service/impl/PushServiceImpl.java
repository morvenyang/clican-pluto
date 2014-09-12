package com.huace.mas.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huace.mas.apns.ApnsService;
import com.huace.mas.dao.DataDao;
import com.huace.mas.dao.UserDao;
import com.huace.mas.service.PushService;

public class PushServiceImpl implements PushService {

	private final static Log log = LogFactory.getLog(PushServiceImpl.class);

	private DataDao dataDao;

	private UserDao userDao;

	private ApnsService apnsService;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setApnsService(ApnsService apnsService) {
		this.apnsService = apnsService;
	}

	@Override
	public void pushMsg() {
		if (log.isInfoEnabled()) {
			log.info("start push message");
		}
	}

}
