package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		List<String> msisdns = new ArrayList<String>();
		for (BroadbandRemind bbr : bbrList) {
			msisdns.add(bbr.getMsisidn());
		}
		List<BroadbandRemind> bbrInDb = this.broadbandRemindDao
				.findBroadbandRemindByMsisdns(msisdns);
		Map<String, BroadbandRemind> bbrMap = new HashMap<String, BroadbandRemind>();
		for (BroadbandRemind bbr : bbrInDb) {
			bbrMap.put(bbr.getMsisidn(), bbr);
		}
		Set<String> processed = new HashSet<String>();
		for (BroadbandRemind bbr : bbrList) {
			if (processed.contains((bbr.getMsisidn()))) {
				continue;
			}
			if (bbrMap.containsKey(bbr.getMsisidn())) {
				BroadbandRemind bbrDb = bbrMap.get(bbr.getMsisidn());
				bbrDb.setExpiredDate(bbr.getExpiredDate());
				bbrDb.setUserName(bbr.getUserName());
				bbrDb.setBroadBandId(bbr.getBroadBandId());
				this.broadbandRemindDao.save(bbrDb);
			} else {
				this.broadbandRemindDao.save(bbr);
			}
			processed.add(bbr.getMsisidn());
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
