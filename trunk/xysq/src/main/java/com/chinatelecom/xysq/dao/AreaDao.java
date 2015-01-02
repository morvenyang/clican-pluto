package com.chinatelecom.xysq.dao;

import java.util.List;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;

public interface AreaDao {

	public List<Area> getAreaTrees();

	public PageList<Community> findCommunityByArea(Area area, int page,
			int pageSize);
	
	public List<Area> getAreasByFullNames(List<String> fullNames);
	
	public void saveArea(Area area);
	
	public void saveCommunity(Community community);

}
