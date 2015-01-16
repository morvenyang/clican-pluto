package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.BroadbandRemind;

public interface BroadbandRemindDao {

	public PageList<BroadbandRemind> findBroadbandRemind(int page, int pageSize);

	public void save(BroadbandRemind bbr);

	public void delete(BroadbandRemind bbr);

	public BroadbandRemind findBroadbandRemindById(Long id);
	
	public List<BroadbandRemind> findBroadbandRemindByMsisdns(List<String> msisdns);

}
