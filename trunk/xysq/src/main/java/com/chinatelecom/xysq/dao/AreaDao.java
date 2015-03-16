package com.chinatelecom.xysq.dao;

import java.util.List;
import java.util.Set;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.model.AdminCommunityRel;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.PosterCommunityRel;
import com.chinatelecom.xysq.model.StoreCommunityRel;

public interface AreaDao {

	public List<Area> getAreaTrees();

	public PageList<Community> findCommunityByArea(Area area, int page,
			int pageSize);
	
	public List<Area> getAreasByFullNames(List<String> fullNames);
	
	public void saveArea(Area area);
	
	public void deleteArea(Area area);
	
	public void saveCommunity(Community community);
	
	public Community findCommunityById(Long id);
	
	public List<Community> findCommunityByIds(Set<Long> ids);
	
	public void deleteCommunity(Community community);
	
	public void deleteAdminCommunityRel(Community community);
	
	public void deleteStoreCommunityRel(Community community);
	
	public void deletePosterCommunityRel(Community community);
	
	public void saveAdminCommunityRel(AdminCommunityRel rel);
	
	public void saveStoreCommunityRel(StoreCommunityRel rel);
	
	public void savePosterCommunityRel(PosterCommunityRel rel);
	
	public List<Area> findCityAreas();
	
	public List<Community> findCommunityByArea(Long areaId);
	
}
