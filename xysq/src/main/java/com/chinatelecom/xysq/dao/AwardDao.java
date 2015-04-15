package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.model.Award;
import com.chinatelecom.xysq.model.AwardHistory;
import com.chinatelecom.xysq.model.AwardStoreRel;

public interface AwardDao {

	public List<Award> findAllAwards();

	public void deleteAward(Award award);

	public void saveAward(Award award);
	
	public void saveAwardHistory(AwardHistory awardHistory);
	
	public Award findAwardById(Long awardId);
	
	public void saveAwardStoreRel(AwardStoreRel awardStoreRel);
	
	public void resetLottery();
}
