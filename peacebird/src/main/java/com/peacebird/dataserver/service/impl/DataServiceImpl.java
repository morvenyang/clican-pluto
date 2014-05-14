package com.peacebird.dataserver.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.peacebird.dataserver.bean.IndexAmountResult;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.service.DataService;

public class DataServiceImpl implements DataService {

	private DataDao dataDao;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	@Override
	public String getCurrentIndexResult() {
		Date yesterday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		yesterday = DateUtils.addDays(yesterday, -1);
		List<IndexAmountResult> indexResults = dataDao.getIndexResult(yesterday);
		
		return null;
	}
	
}
