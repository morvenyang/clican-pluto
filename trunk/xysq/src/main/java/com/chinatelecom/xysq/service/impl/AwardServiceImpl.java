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

	@Override
	public void deleteAward(Award award) {
		this.awardDao.deleteAward(award);
	}

	@Override
	public void saveAward(Award award) {
		if (award.getId() == null) {
			award.setAmount(award.getTotalAmount());
		}
		this.awardDao.saveAward(award);
	}

}
