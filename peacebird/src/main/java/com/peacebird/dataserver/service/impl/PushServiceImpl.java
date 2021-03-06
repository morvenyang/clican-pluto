package com.peacebird.dataserver.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.peacebird.dataserver.apns.ApnsService;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.dao.UserDao;
import com.peacebird.dataserver.model.DayStatus;
import com.peacebird.dataserver.service.PushService;

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
		Date yesterday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		yesterday = DateUtils.addDays(yesterday, -1);
		DayStatus ds = dataDao.getDayStatus(yesterday);
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		if (ds != null) {
			if (ds.getPush() != null && ds.getPush().intValue() == 1) {
				if (log.isDebugEnabled()) {
					log.debug("The message has been pushed");
					return;
				}
			}
			if (ds.getStatus() != null && ds.getStatus().intValue() != 0) {
				if (log.isInfoEnabled()) {
					log.info("Find DayStatus for " + sdf.format(yesterday)
							+ ",");
				}
				String msg = sdf.format(yesterday) + "数据已生成，请查阅";
				log.info("push message[" + msg + "]");
				List<String> tokens = this.userDao.findAllActiveToken();
				Set<String> singleTokens = new HashSet<String>(tokens);
				for (String token : singleTokens) {
					try {
						if (StringUtils.isNotEmpty(token)) {
							log.info("push message[" + msg + "] to token ["
									+ token + "]");
							apnsService.sendMessage(msg, token);
						}
					} catch (Exception e) {
						log.error("", e);
					}
				}
				ds.setPush(1);
				dataDao.saveDayStatus(ds);
			} else {
				if (log.isInfoEnabled()) {
					log.info("The DayStatus is not ready, we can't push message ["
							+ sdf.format(yesterday) + "]");
				}
			}
		} else {
			log.warn("The DayStatus is not existed, we can't push message ["
					+ sdf.format(yesterday) + "]");
		}

	}

}
