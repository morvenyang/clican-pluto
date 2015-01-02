package com.chinatelecom.xysq.service;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.BroadbandRemind;

public interface BroadbandRemindService {

	public PageList<BroadbandRemind> findBroadbandRemind(int page, int pageSize);
}
