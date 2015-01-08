package com.chinatelecom.xysq.service.impl;

import java.util.List;

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

	@Override
	public void saveBoradbandReminds(List<BroadbandRemind> bbrList) {
		for(BroadbandRemind bbr:bbrList){
			this.broadbandRemindDao.save(bbr);
		}
	}

	@Override
	public void save(BroadbandRemind bbr) {
		this.broadbandRemindDao.save(bbr);
	}

	@Override
	public void delete(BroadbandRemind bbr) {
		this.broadbandRemindDao.delete(bbr);
	}

	@Override
	public BroadbandRemind findBroadbandRemindById(Long id) {
		return broadbandRemindDao.findBroadbandRemindById(id);
	}

}
