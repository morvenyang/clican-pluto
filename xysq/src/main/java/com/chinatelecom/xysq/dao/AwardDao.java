package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.model.Award;

public interface AwardDao {

	public List<Award> findAllAwards();

	public void deleteAward(Award award);

	public void saveAward(Award award);
	
	
	public void resetLottery();
}
