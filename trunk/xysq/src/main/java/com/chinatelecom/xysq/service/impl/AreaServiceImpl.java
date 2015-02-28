package com.chinatelecom.xysq.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.dao.AreaDao;
import com.chinatelecom.xysq.json.AreaJson;
import com.chinatelecom.xysq.json.CommunityJson;
import com.chinatelecom.xysq.model.AdminCommunityRel;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.PosterCommunityRel;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.StoreCommunityRel;
import com.chinatelecom.xysq.model.User;
import com.chinatelecom.xysq.service.AreaService;
import com.github.stuxuhai.jpinyin.PinyinFormat;
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
				int level = StringUtils.countMatches(fullName, "/") + 1;
				area.setLevel(level);
				area.setShortPinyin(PinyinHelper.getShortPinyin(area.getName()));
				area.setPinyin(PinyinHelper.convertToPinyinString(
						area.getName(), "", PinyinFormat.WITHOUT_TONE));
				areaDao.saveArea(area);
				areaMap.put(area.getFullName(), area);
			} else {
				Area area = areaMap.get(fullName);
				area.setShortPinyin(PinyinHelper.getShortPinyin(area.getName()));
				area.setPinyin(PinyinHelper.convertToPinyinString(
						area.getName(), "", PinyinFormat.WITHOUT_TONE));
				areaDao.saveArea(area);
			}
		}
		return areaMap;
	}

	@Override
	public void saveCommunity(Community community) {
		community.setShortPinyin(PinyinHelper.getShortPinyin(community
				.getName()));
		community.setPinyin(PinyinHelper.convertToPinyinString(
				community.getName(), "", PinyinFormat.WITHOUT_TONE));
		this.areaDao.saveCommunity(community);
	}

	@Override
	public void deleteCommunity(Community community) {
		this.areaDao.deleteCommunity(community);
	}

	@Override
	public void mergeCommunities(Map<String, List<Community>> communityMap,
			Map<String, Area> areaMap) {
		for (String areaFullName : communityMap.keySet()) {
			Area area = areaMap.get(areaFullName);
			Map<String, Community> communities = new HashMap<String, Community>();
			if (area.getCityComminitySet() != null) {
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
				mergeC.setArea(area);
				if (area.getLevel() == 2) {
					mergeC.setCity(area);
				} else {
					mergeC.setCity(area.getParent());
				}
				this.saveCommunity(mergeC);
			}
		}
	}

	@Override
	public Community findCommunityById(Long id) {
		return this.areaDao.findCommunityById(id);
	}

	@Override
	public void saveComminity(Community community, List<User> admins,
			List<Store> stores, List<Poster> posters) {
		if (community.getId() != null) {
			this.areaDao.deleteAdminCommunityRel(community);
			this.areaDao.deletePosterCommunityRel(community);
			this.areaDao.deleteStoreCommunityRel(community);
		}
		saveCommunity(community);
		for (User user : admins) {
			AdminCommunityRel acr = new AdminCommunityRel();
			acr.setAdmin(user);
			acr.setCommunity(community);
			this.areaDao.saveAdminCommunityRel(acr);
		}
		for (Store store : stores) {
			StoreCommunityRel scr = new StoreCommunityRel();
			scr.setStore(store);
			scr.setCommunity(community);
			this.areaDao.saveStoreCommunityRel(scr);
		}
		for (int i = 0; i < posters.size(); i++) {
			Poster poster = posters.get(i);
			PosterCommunityRel pcr = new PosterCommunityRel();
			pcr.setPoster(poster);
			pcr.setDisplayIndex(i);
			pcr.setCommunity(community);
			this.areaDao.savePosterCommunityRel(pcr);
		}
	}

	@Override
	public String queryCityAreas() {
		List<Area> areaTrees = this.areaDao.findCityAreas();
		List<AreaJson> result = new ArrayList<AreaJson>();
		for (Area area : areaTrees) {
			AreaJson aj = new AreaJson();
			aj.setId(area.getId());
			aj.setName(area.getName());
			aj.setPinyin(area.getPinyin());
			aj.setShortPinyin(area.getShortPinyin());
			aj.setFullName(area.getFullName());
			result.add(aj);
		}
		return JSONArray.fromObject(result).toString();
	}

	@Override
	public String queryCommunityByArea(Long areaId) {
		List<Community> communityList = this.areaDao
				.findCommunityByArea(areaId);
		List<CommunityJson> result = new ArrayList<CommunityJson>();
		for (Community community : communityList) {
			CommunityJson cj = new CommunityJson();
			cj.setId(community.getId());
			cj.setName(community.getName());
			cj.setPinyin(community.getPinyin());
			cj.setShortPinyin(community.getShortPinyin());
			cj.setDetailAddress(community.getDetailAddress());
			result.add(cj);
		}
		return JSONArray.fromObject(result).toString();
	}

	private void populateFullNameForArea(Area area) {
		String fullName = area.getName();
		Area temp = area;
		while (temp.getParent() != null) {
			fullName = temp.getParent().getName() + "/" + fullName;
			temp = temp.getParent();
		}
		area.setFullName(fullName);
	}

	@Override
	public void saveArea(Area area) {
		populateFullNameForArea(area);
		area.setShortPinyin(PinyinHelper.getShortPinyin(area.getName()));
		area.setPinyin(PinyinHelper.convertToPinyinString(area.getName(), "",
				PinyinFormat.WITHOUT_TONE));
		areaDao.saveArea(area);
	}

	private void saveAreaForAllChildren(Area area){
		populateFullNameForArea(area);
		areaDao.saveArea(area);
		if(area.getChildren()!=null){
			for(Area child:area.getChildren()){
				this.saveAreaForAllChildren(child);
			}
		}
	}
	@Override
	public void renameArea(Area area) {
		area.setShortPinyin(PinyinHelper.getShortPinyin(area.getName()));
		area.setPinyin(PinyinHelper.convertToPinyinString(area.getName(), "",
				PinyinFormat.WITHOUT_TONE));
		saveAreaForAllChildren(area);
	}

	@Override
	public void deleteArea(Area area) {
		this.areaDao.deleteArea(area);
	}

}
