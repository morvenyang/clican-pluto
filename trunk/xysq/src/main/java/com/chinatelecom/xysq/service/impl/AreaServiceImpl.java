package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.AreaDao;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.service.AreaService;
import com.github.stuxuhai.jpinyin.PinyinHelper;

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
	public PageList<Community> findCommunityByArea(Area area, int page,
			int pageSize) {
		return areaDao.findCommunityByArea(area, page, pageSize);
	}

	@Override
	public Map<String, Area> mergeAreas(List<String> fullNames) {
		List<Area> areas = areaDao.getAreasByFullNames(fullNames);
		Map<String, Area> areaMap = new HashMap<String, Area>();
		for (Area area : areas) {
			areaMap.put(area.getFullName(), area);
		}
		for (String fullName : fullNames) {
			if (!areaMap.containsKey(fullName)) {
				Area area = new Area();
				area.setCityComminitySet(new HashSet<Community>());
				area.setChildren(new ArrayList<Area>());
				area.setFullName(fullName);
				if (fullName.indexOf("/") != -1) {
					area.setName(StringUtils.substringAfterLast(fullName, "/"));
					String parentFullName = StringUtils.substringBeforeLast(
							fullName, "/");
					Area parent = areaMap.get(parentFullName);
					area.setParent(parent);
				} else {
					area.setName(fullName);
				}
				int level = StringUtils.countMatches(fullName, "/")+1;
				area.setLevel(level);
				area.setPinyin(PinyinHelper.getShortPinyin(area.getName()));
				areaDao.saveArea(area);
				areaMap.put(area.getFullName(), area);
			}
		}
		return areaMap;
	}

	@Override
	public void saveCommunity(Community community) {
		community.setPinyin(PinyinHelper.getShortPinyin(community.getName()));
		this.areaDao.saveCommunity(community);
	}

	@Override
	public void mergeCommunities(Map<String, List<Community>> communityMap,
			Map<String, Area> areaMap) {
		for (String areaFullName : communityMap.keySet()) {
			Area area = areaMap.get(areaFullName);
			Map<String, Community> communities = new HashMap<String, Community>();
			if(area.getCityComminitySet()!=null){
				for (Community c : area.getCityComminitySet()) {
					communities.put(c.getName(), c);
				}
			}
			for (Community c : communityMap.get(areaFullName)) {
				Community mergeC = communities.get(c.getName());
				if (mergeC == null) {
					mergeC = c;
				} else {
					mergeC.setDetailAddress(c.getDetailAddress());
				}
				mergeC.setCity(area);
				this.saveCommunity(mergeC);
			}
		}
	}

}
