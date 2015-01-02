package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.bean.PagingList;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;

public interface AreaDao {

	public List<Area> getAreaTrees();

	public PagingList<Community> findCommunityByArea(Area area, int page,
			int pageSize);

}
