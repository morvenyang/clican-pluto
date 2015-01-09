package com.chinatelecom.xysq.service;

import java.util.List;
import java.util.Map;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.User;

public interface AreaService {

	public List<Area> getAreaTrees();

	public PageList<Community> findCommunityByArea(Area area, int page,
			int pageSize);

	public Map<String, Area> mergeAreas(List<String> fullNames);

	public void saveCommunity(Community community);
	
	public void deleteCommunity(Community community);

	public void mergeCommunities(Map<String, List<Community>> communityMap,
			Map<String, Area> areaMap);

	public Community findCommunityById(Long id);
	
	public void saveComminity(Community community,List<User> admins,List<Store> stores,List<Poster> posters);
}
