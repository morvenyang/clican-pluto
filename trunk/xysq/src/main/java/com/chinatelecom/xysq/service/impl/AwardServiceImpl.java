package com.chinatelecom.xysq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.dao.AwardDao;
import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.json.LotteryJson;
import com.chinatelecom.xysq.model.Award;
import com.chinatelecom.xysq.model.AwardHistory;
import com.chinatelecom.xysq.model.AwardStoreRel;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.AwardService;

public class AwardServiceImpl implements AwardService {

	private AwardDao awardDao;

	private UserDao userDao;
	
	private SpringProperty springProperty;

	private Map<Integer, Float> moneyProbability = new TreeMap<Integer, Float>();

	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void init(){
		String moneyProbabilityStr=springProperty.getMoneyProbability();
		for(String pair:moneyProbabilityStr.split(";")){
			Integer money = Integer.parseInt(pair.split(":")[0]);
			Float probablity =Float.parseFloat(pair.split(":")[1]);
			moneyProbability.put(money, probablity);
		}
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
		this.awardDao.saveAward(award);
		Set<AwardStoreRel> awardStoreRelSet=award.getAwardStoreRelSet();
		for(AwardStoreRel asr:awardStoreRelSet){
			this.awardDao.saveAwardStoreRel(asr);
		}
	}

	public void resetLottery() {
		this.awardDao.resetLottery();
	}

	@Override
	public Award findAwardById(Long awardId) {
		return this.awardDao.findAwardById(awardId);
	}

	private int getRandomMoney() {
		double i = new Random().nextInt(100) / 100.0;
		float t = 0;
		Integer found = null;
		for (Integer money : moneyProbability.keySet()) {
			Float f = moneyProbability.get(money);
			t += f;
			if (i <= f) {
				found = money;
			}
		}
		if (found == null) {
			return 10;
		}
		return found;
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
		int totalMoney = user.getMoney();
		LotteryJson lotteryJson = new LotteryJson();
		if (lottery > 0) {
			money = getRandomMoney();
			totalMoney += money;
			lottery--;
			user.setLottery(lottery);
			user.setMoney(totalMoney);
			userDao.saveUser(user);
			AwardHistory awardHistory = new AwardHistory();
			awardHistory.setDate(new Date());
			awardHistory.setLottery(true);
			awardHistory.setMoney(money);
			awardDao.saveAwardHistory(awardHistory);
			lotteryJson.setShare(false);
		} else {
			lotteryJson.setShare(user.isShareLottery());
		}
		lotteryJson.setLottery(lottery);
		lotteryJson.setMoney(money);
		lotteryJson.setTotalMoney(totalMoney);
		return JSONObject.fromObject(lotteryJson).toString();
	}

}
