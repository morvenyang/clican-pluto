package com.chinatelecom.xysq.service;

import java.util.List;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;

public interface AreaService {

	public List<Area> getAreaTrees();
	
	public PageList<Community> findCommunityByArea(Area area, int page,
			int pageSize);
}
