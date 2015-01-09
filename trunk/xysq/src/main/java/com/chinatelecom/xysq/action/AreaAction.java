package com.chinatelecom.xysq.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.security.Restrict;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.chinatelecom.xysq.bean.EmptyPageList;
import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.bean.SuggestionBean;
import com.chinatelecom.xysq.model.AdminCommunityRel;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.PosterCommunityRel;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.StoreCommunityRel;
import com.chinatelecom.xysq.model.User;

@Scope(ScopeType.PAGE)
@Name("areaAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AreaAction extends PageListAction<Community> {

	private List<Area> areaTrees;

	private PageListDataModel<Community> communitiesBySelectedNode;

	private Area selectedArea;

	private List<String> areaFullNames;

	private Map<String, List<Community>> communityMap;

	private Community community;

	private Long selectedAdminId;

	private List<User> selectedAdmins;

	private PageListDataModel<Poster> posterDataModel;

	private PageListDataModel<Store> storeDataModel;

	private List<Poster> selectedPosters;
	private List<Store> selectedStores;

	public void listAreaTrees() {
		this.page = 1;
		this.areaTrees = this.getAreaService().getAreaTrees();
		if (this.areaTrees.size() != 0) {
			selectedArea = this.areaTrees.get(0);
		} else {
			selectedArea = null;
		}

		this.refresh();
	}

	public void selectArea(Area area) {
		this.selectedArea = area;
		this.refresh();
	}

	public void selectPoster(Poster poster) {
		if (!this.selectedPosters.contains(poster)) {
			this.selectedPosters.add(poster);
		}
	}

	public void unselectPoster(Poster poster) {
		this.selectedPosters.remove(poster);
	}

	public void selectStore(Store store) {
		if (!this.selectedStores.contains(store)) {
			this.selectedStores.add(store);
		}
	}

	public void unselectStore(Store store) {
		this.selectedStores.remove(store);
	}

	public void prepareStores() {
		storeDataModel = new PageListDataModel<Store>(PAGE_SIZE) {
			@Override
			public PageList<Store> fetchPage(int page, int pageSize) {
				return getStoreService().findStoreByOwner(null, 1, PAGE_SIZE);
			}
		};
	}

	public void preparePosters() {
		this.posterDataModel = new PageListDataModel<Poster>(25) {
			@Override
			public PageList<Poster> fetchPage(int page, int pageSize) {
				return getPosterService().findPoster(page, pageSize);
			}
		};
	}

	private void refresh() {
		communitiesBySelectedNode = new PageListDataModel<Community>(
				this.getPageSize()) {
			@Override
			public PageList<Community> fetchPage(int page, int pageSize) {
				if (selectedArea != null) {
					return getAreaService().findCommunityByArea(selectedArea,
							page, PAGE_SIZE);
				} else {
					return new EmptyPageList<Community>(page, PAGE_SIZE);
				}
			}
		};
	}

	public void addCommunity() {
		this.community = new Community();
		this.selectedAdmins = new ArrayList<User>();
		this.selectedPosters = new ArrayList<Poster>();
		this.selectedStores = new ArrayList<Store>();
	}

	public void editCommunity(Community community) {
		this.community = this.getAreaService().findCommunityById(
				community.getId());
		this.selectedAdmins = new ArrayList<User>();
		for (AdminCommunityRel adminCommunityRel : this.community
				.getAdminCommunityRelSet()) {
			this.selectedAdmins.add(adminCommunityRel.getAdmin());
		}
		this.selectedPosters = new ArrayList<Poster>();
		for (PosterCommunityRel posterCommunityRel : this.community
				.getPosterCommunityRelList()) {
			this.selectedPosters.add(posterCommunityRel.getPoster());
		}
		this.selectedStores = new ArrayList<Store>();
		for (StoreCommunityRel storeCommunityRel : this.community
				.getStoreCommunityRelSet()) {
			this.selectedStores.add(storeCommunityRel.getStore());
		}
	}

	public List<SuggestionBean> autoCompleteAdmins(Object suggest) {
		List<User> users = this.getUserService()
				.findAreaAdmin((String) suggest);
		List<SuggestionBean> suggestionBeans = new ArrayList<SuggestionBean>();
		for (User user : users) {
			SuggestionBean ps = new SuggestionBean(user.getId(),
					user.getUserName());
			suggestionBeans.add(ps);
		}
		return suggestionBeans;

	}

	public List<SuggestionBean> autoCompleteStores(Object suggest) {
		List<Store> stores = this.getStoreService().findStores(
				this.getIdentity().getUser().getId(), (String) suggest);
		List<SuggestionBean> suggestionBeans = new ArrayList<SuggestionBean>();
		for (Store store : stores) {
			SuggestionBean ps = new SuggestionBean(store.getId(),
					store.getName());
			suggestionBeans.add(ps);
		}
		return suggestionBeans;

	}

	public void selectAdmin() {
		User user = this.getUserService().findUserById(this.selectedAdminId);
		this.selectedAdmins.add(user);
	}

	public void unselectAdmin(User user) {
		this.selectedAdmins.remove(user);
	}

	public void saveCommunity() {
		this.getAreaService().saveComminity(community, this.selectedAdmins,
				this.selectedStores, this.selectedPosters);
	}
	
	public void deleteCommunity(Community community) {
		this.getAreaService().deleteCommunity(community);
		this.refresh();
	}

	public synchronized void importExcel(UploadEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("import excel");
		}
		List<UploadItem> itemList = event.getUploadItems();
		UploadItem item = itemList.get(0);
		File excelFile = item.getFile();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(excelFile);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			int i = 1;
			areaFullNames = new ArrayList<String>();
			Set<String> areaFullNameSet = new HashSet<String>();
			communityMap = new HashMap<String, List<Community>>();
			while (true) {
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				Cell cell = null;
				cell = row.getCell(1);
				String area1 = null;
				if (cell != null) {
					area1 = row.getCell(1).getStringCellValue();
					if (StringUtils.isNotEmpty(area1)) {
						area1 = area1.trim();
					}
				}

				cell = row.getCell(2);
				String area2 = null;
				if (cell != null) {
					area2 = row.getCell(2).getStringCellValue();
					if (StringUtils.isNotEmpty(area2)) {
						area2 = area2.trim();
					}
				}

				cell = row.getCell(3);
				String area3 = null;
				if (cell != null) {
					area3 = row.getCell(3).getStringCellValue();
					if (StringUtils.isNotEmpty(area3)) {
						area3 = area3.trim();
					}
				}

				cell = row.getCell(4);
				String community = null;
				if (cell != null) {
					community = row.getCell(4).getStringCellValue();
					if (StringUtils.isNotEmpty(community)) {
						community = community.trim();
					}
				}

				cell = row.getCell(5);
				String detailAddress = null;
				if (cell != null) {
					detailAddress = row.getCell(5).getStringCellValue();
					if (StringUtils.isNotEmpty(detailAddress)) {
						detailAddress = detailAddress.trim();
					}
				}

				String areaFullName = area1;
				if (!areaFullNameSet.contains(areaFullName)) {
					areaFullNames.add(areaFullName);
					areaFullNameSet.add(areaFullName);
				}
				if (StringUtils.isNotEmpty(area2)) {
					areaFullName = area1 + "/" + area2;
					if (!areaFullNameSet.contains(areaFullName)) {
						areaFullNames.add(areaFullName);
						areaFullNameSet.add(areaFullName);
					}
					if (StringUtils.isNotEmpty(area3)) {
						areaFullName = area1 + "/" + area2 + "/" + area3;
						if (!areaFullNameSet.contains(areaFullName)) {
							areaFullNames.add(areaFullName);
							areaFullNameSet.add(areaFullName);
						}
					}
				}
				if (StringUtils.isNotEmpty(community)) {
					if (!communityMap.containsKey(areaFullName)) {
						communityMap.put(areaFullName,
								new ArrayList<Community>());
					}
					List<Community> communityList = communityMap
							.get(areaFullName);
					Community c = new Community();
					c.setName(community);
					c.setDetailAddress(detailAddress);
					communityList.add(c);
				}
				i++;
			}

		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					log.error("", e);
				}

			}
		}
	}

	public void saveImportExcel() {
		if (log.isDebugEnabled()) {
			log.debug("save import excel");
		}
		Map<String, Area> areaMap = this.getAreaService().mergeAreas(
				areaFullNames);
		getAreaService().mergeCommunities(communityMap, areaMap);
		refresh();
	}

	@BypassInterceptors
	public List<Area> getAreaTrees() {
		return areaTrees;
	}

	public void setAreaTrees(List<Area> areaTrees) {
		this.areaTrees = areaTrees;
	}

	public PageListDataModel<Community> getCommunitiesBySelectedNode() {
		return communitiesBySelectedNode;
	}

	public void setCommunitiesBySelectedNode(
			PageListDataModel<Community> communitiesBySelectedNode) {
		this.communitiesBySelectedNode = communitiesBySelectedNode;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	@Override
	public PageListDataModel<Community> getDefaultDataModel() {
		return communitiesBySelectedNode;
	}

	public Long getSelectedAdminId() {
		return selectedAdminId;
	}

	public void setSelectedAdminId(Long selectedAdminId) {
		this.selectedAdminId = selectedAdminId;
	}

	public List<User> getSelectedAdmins() {
		return selectedAdmins;
	}

	public void setSelectedAdmins(List<User> selectedAdmins) {
		this.selectedAdmins = selectedAdmins;
	}

	public PageListDataModel<Poster> getPosterDataModel() {
		return posterDataModel;
	}

	public void setPosterDataModel(PageListDataModel<Poster> posterDataModel) {
		this.posterDataModel = posterDataModel;
	}

	public int getPosterPage() {
		return this.getPosterDataModel().getRowIndex() / 25 + 1;
	}

	public void setPosterPage(int page) {
		this.getPosterDataModel().setRowIndex((page - 1) * 25);
	}

	public int getStorePage() {
		return this.getStoreDataModel().getRowIndex() / 25 + 1;
	}

	public void setStorePage(int page) {
		this.getStoreDataModel().setRowIndex((page - 1) * 25);
	}

	public List<Poster> getSelectedPosters() {
		return selectedPosters;
	}

	public void setSelectedPosters(List<Poster> selectedPosters) {
		this.selectedPosters = selectedPosters;
	}

	public PageListDataModel<Store> getStoreDataModel() {
		return storeDataModel;
	}

	public void setStoreDataModel(PageListDataModel<Store> storeDataModel) {
		this.storeDataModel = storeDataModel;
	}

	public List<Store> getSelectedStores() {
		return selectedStores;
	}

	public void setSelectedStores(List<Store> selectedStores) {
		this.selectedStores = selectedStores;
	}

}
