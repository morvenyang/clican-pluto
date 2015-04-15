package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import net.sf.json.JSONObject;

import com.chinatelecom.xysq.bean.SpringProperty;
import com.chinatelecom.xysq.dao.AwardDao;
import com.chinatelecom.xysq.dao.UserDao;
import com.chinatelecom.xysq.json.AwardJson;
import com.chinatelecom.xysq.json.AwardUserJson;
import com.chinatelecom.xysq.json.ExchangeAwardJson;
import com.chinatelecom.xysq.json.LotteryJson;
import com.chinatelecom.xysq.json.StoreJson;
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

	public void init() {
		String moneyProbabilityStr = springProperty.getMoneyProbability();
		for (String pair : moneyProbabilityStr.split(";")) {
			Integer money = Integer.parseInt(pair.split(":")[0]);
			Float probablity = Float.parseFloat(pair.split(":")[1]);
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
		Set<AwardStoreRel> awardStoreRelSet = award.getAwardStoreRelSet();
		if (award.getId() != null) {
			List<Long> relIds = new ArrayList<Long>();
			for (AwardStoreRel asr : awardStoreRelSet) {
				if (asr.getId() != null) {
					relIds.add(asr.getId());
				}
			}
			this.awardDao.deleteAwardSotreRel(award.getId(), relIds);
		}
		for (AwardStoreRel asr : awardStoreRelSet) {
			asr.setAward(award);
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
	public synchronized String lottery(Long userId) {
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

	@Override
	public String queryAwardUser(Long userId) {
		User user = userDao.findUserById(userId);
		AwardUserJson auj = new AwardUserJson();
		List<Award> awards = awardDao.findActiveAwards();
		List<AwardJson> ajs = new ArrayList<AwardJson>();
		for (Award award : awards) {
			AwardJson aj = new AwardJson();
			aj.setId(award.getId());
			aj.setCost(award.getCost());
			aj.setName(award.getName());
			aj.setRealGood(award.isRealGood());
			int amount = 0;
			for (AwardStoreRel rel : award.getAwardStoreRelSet()) {
				amount += rel.getAmount();
			}
			aj.setAmount(amount);
			ajs.add(aj);
		}
		auj.setAwards(ajs);
		if (user.getMoney() == null) {
			auj.setMoney(0);
		} else {
			auj.setMoney(user.getMoney());
		}
		return JSONObject.fromObject(auj).toString();
	}

	@Override
	public synchronized String exchangeAward(Long awardId, Long userId) {
		ExchangeAwardJson eaj = new ExchangeAwardJson();
		Award award = awardDao.findAwardById(awardId);
		int amount = 0;
		for (AwardStoreRel rel : award.getAwardStoreRelSet()) {
			amount += rel.getAmount();
		}
		if(amount<=0){
			eaj.setSuccess(false);
			eaj.setMessage("该物品已经全部兑换完了");
		}else{
			User user = userDao.findUserById(userId);
			if (user.getMoney() == null || user.getMoney() < award.getCost()) {
				eaj.setSuccess(false);
				eaj.setMessage("你的流量币不够兑换该物品");
			} else {
				AwardHistory awardHistory = new AwardHistory();
				awardHistory.setAward(award);
				awardHistory.setDate(new Date());
				awardHistory.setLottery(false);
				awardHistory.setMoney(-award.getCost());
				if(award.isRealGood()){
					awardHistory.setReceived(false);
				}else{
					awardHistory.setReceived(true);
				}
				
				awardHistory.setUser(user);
				awardHistory.setCode(UUID.randomUUID().toString());
				this.awardDao.saveAwardHistory(awardHistory);
				user.setMoney(user.getMoney()-award.getCost());
				this.userDao.saveUser(user);
				eaj.setSuccess(true);
				eaj.setMessage("兑换成功");
				eaj.setName(award.getName());
				String code = generateRandomCode();
				eaj.setCode(code);
				eaj.setId(awardHistory.getId());
				eaj.setReceived(awardHistory.isReceived());
				eaj.setRealGood(award.isRealGood());
				List<StoreJson> stores = new ArrayList<StoreJson>();
				for(AwardStoreRel rel:award.getAwardStoreRelSet()){
					StoreJson sj = new StoreJson();
					sj.setName(rel.getStore().getName());
					sj.setAddress(rel.getStore().getAddress());
					sj.setTel(rel.getStore().getTel());
					sj.setId(rel.getStore().getId());
					stores.add(sj);
				}
				eaj.setStores(stores);
			}
		}	
		return JSONObject.fromObject(eaj).toString();
	}

	private String generateRandomCode(){
		List<Character> charList =new ArrayList<Character>(); 
		for(char c='0';c<='9';c++){
			charList.add(c);
		}
		for(char c='a';c<='z';c++){
			charList.add(c);
		}
		for(char c='A';c<='Z';c++){
			charList.add(c);
		}
		String result = "";
		
		for(int i=0;i<10;i++){
			result +=charList.get(new Random().nextInt(charList.size()));
		}
		return result;
	}
}
