package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.model.Award;
import com.chinatelecom.xysq.model.AwardHistory;

public interface AwardDao {

	public List<Award> findAllAwards();

	public void deleteAward(Award award);

	public void saveAward(Award award);
	
	public void saveAwardHistory(AwardHistory awardHistory);
	
	public void resetLottery();
}
