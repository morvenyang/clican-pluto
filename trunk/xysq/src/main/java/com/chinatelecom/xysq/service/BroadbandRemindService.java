package com.chinatelecom.xysq.service;

import java.util.List;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.BroadbandRemind;

public interface BroadbandRemindService {

	public PageList<BroadbandRemind> findBroadbandRemind(int page, int pageSize);
	
	public void saveBoradbandReminds(List<BroadbandRemind> bbrList);
	
	public void save(BroadbandRemind bbr);
	
	public void delete(BroadbandRemind bbr);

	public BroadbandRemind findBroadbandRemindById(Long id);
}
