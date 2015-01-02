package com.chinatelecom.xysq.service.impl;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.BroadbandRemindDao;
import com.chinatelecom.xysq.model.BroadbandRemind;
import com.chinatelecom.xysq.service.BroadbandRemindService;

public class BroadbandRemindServiceImpl implements BroadbandRemindService {

	private BroadbandRemindDao broadbandRemindDao;
	
	public void setBroadbandRemindDao(BroadbandRemindDao broadbandRemindDao) {
		this.broadbandRemindDao = broadbandRemindDao;
	}

	@Override
	public PageList<BroadbandRemind> findBroadbandRemind(int page, int pageSize) {
		return broadbandRemindDao.findBroadbandRemind(page, pageSize);
	}

}
