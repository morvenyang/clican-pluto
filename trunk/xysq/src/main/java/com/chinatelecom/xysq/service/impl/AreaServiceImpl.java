package com.chinatelecom.xysq.service.impl;

import java.util.List;

import com.chinatelecom.xysq.bean.PagingList;
import com.chinatelecom.xysq.dao.AreaDao;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.service.AreaService;

public class AreaServiceImpl implements AreaService {

	private AreaDao areaDao;

	public void setAreaDao(AreaDao areaDao) {
		this.areaDao = areaDao;
	}

	@Override
	public List<Area> getAreaTrees() {
		return areaDao.getAreaTrees();
	}

	@Override
	public PagingList<Community> findCommunityByArea(Area area, int page,
			int pageSize) {
		return areaDao.findCommunityByArea(area, page,pageSize);
	}
	
	
}
