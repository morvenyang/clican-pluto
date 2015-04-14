package com.chinatelecom.xysq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;

import com.chinatelecom.xysq.dao.AwardDao;
import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.json.LotteryJson;
import com.chinatelecom.xysq.model.Award;
import com.chinatelecom.xysq.model.AwardHistory;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.AwardService;

public class AwardServiceImpl implements AwardService {

	private AwardDao awardDao;

	private UserDao userDao;

	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
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

	public void resetLottery() {
		this.awardDao.resetLottery();
	}

	@Override
	public String lottery(Long userId) {
		User user = userDao.findUserById(userId);
		int lottery = 0;
		if (user.getLottery() == null || user.getLottery() <= 0) {
			lottery = 0;
		} else {
			lottery = user.getLottery();
		}
		int money = -1;
		if (lottery > 0) {
			money = new Random(100).nextInt();
			lottery--;
			user.setLottery(lottery);
			userDao.saveUser(user);
			AwardHistory awardHistory = new AwardHistory();
			awardHistory.setDate(new Date());
			awardHistory.setLottery(true);
			awardHistory.setMoney(money);
			awardDao.saveAwardHistory(awardHistory);
		}
		LotteryJson lotteryJson = new LotteryJson();
		lotteryJson.setLottery(lottery);
		lotteryJson.setMoney(money);
		return JSONArray.fromObject(lotteryJson).toString();
	}

}
