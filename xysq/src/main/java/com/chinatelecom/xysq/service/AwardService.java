package com.chinatelecom.xysq.service;

import java.util.List;

import com.chinatelecom.xysq.model.Award;

public interface AwardService {

	
	public List<Award> findAllAwards();
	
	public void deleteAward(Award award);
	
	public void saveAward(Award award);
}