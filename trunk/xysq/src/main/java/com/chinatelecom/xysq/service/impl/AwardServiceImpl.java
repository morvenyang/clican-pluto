package com.chinatelecom.xysq.service.impl;

import java.util.List;

import com.chinatelecom.xysq.dao.AwardDao;
import com.chinatelecom.xysq.model.Award;
import com.chinatelecom.xysq.service.AwardService;

public class AwardServiceImpl implements AwardService {

	private AwardDao awardDao;

	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}

	@Override
	public List<Award> findAllAwards() {
		return awardDao.findAllAwards();
	}
	
	
}
