package com.peacebird.dataserver.service.impl;

import com.peacebird.dataserver.bean.IndexResult;
import com.peacebird.dataserver.dao.DataDao;
import com.peacebird.dataserver.service.DataService;

public class DataServiceImpl implements DataService {

	private DataDao dataDao;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	@Override
	public IndexResult getCurrentIndexResult() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
