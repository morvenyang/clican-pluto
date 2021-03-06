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
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.chinatelecom.xysq.bean.EmptyPageList;
import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.bean.SuggestionBean;
import com.chinatelecom.xysq.enumeration.InnerModule;
import com.chinatelecom.xysq.enumeration.NoticeCategory;
import com.chinatelecom.xysq.model.AdminCommunityRel;
import com.chinatelecom.xysq.model.AnnouncementAndNotice;
import com.chinatelecom.xysq.model.AnnouncementAndNoticeContent;
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;
import com.chinatelecom.xysq.model.Image;
import com.chinatelecom.xysq.model.Poster;
import com.chinatelecom.xysq.model.PosterCommunityRel;
import com.chinatelecom.xysq.model.Store;
import com.chinatelecom.xysq.model.StoreCommunityRel;
import com.chinatelecom.xysq.model.User;

@Scope(ScopeType.PAGE)
@Name("areaAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AreaAction extends PageListAction<Community> {

	@In(required = true)
	FacesMessages statusMessages;

	private List<Area> areaTrees;

	private PageListDataModel<Community> communitiesBySelectedNode;

	private Area selectedArea;
	private Area area;

	private List<String> areaFullNames;

	private Map<String, List<Community>> communityMap;

	private Community community;

	private Long selectedAdminId;

	private List<User> selectedAdmins;

	private PageListDataModel<Poster> posterDataModel;

	private PageListDataModel<Store> storeDataModel;

	private List<Poster> selectedPosters;
	private List<Store> selectedStores;

	private boolean announcement = false;

	private List<AnnouncementAndNotice> announcementAndNotices;

	private AnnouncementAndNotice announcementAndNotice;
	
	private List<NoticeCategory> noticeCategories;
	
	private List<AnnouncementAndNoticeContent> announcementAndNoticeContents;

	public void listAreaTrees() {
		this.page = 1;
		this.areaTrees = this.getAreaService().getAreaTrees();
		if (this.areaTrees.size() != 0) {
			selectedArea = this.areaTrees.get(0);
		} else {
			selectedArea = null;
		}
		this.noticeCategories = NoticeCategory.getNoticeCategories();
		this.refresh();
	}

	public void addAreaForLevel(int level){
		area = new Area();
		area.setLevel(1);
	}
	
	public void addAreaWithParent(Area parent){
		area = new Area();
		area.setParent(parent);
		area.setLevel(parent.getLevel()+1);
	}
	
	public void renameArea(Area area){
		this.area = area;
	}
	
	public void deleteArea(Area area){
		this.getAreaService().deleteArea(area);
		listAreaTrees();
	}
	public void selectArea(Area area) {
		this.selectedArea = area;
		this.refresh();
	}

	public void saveArea(){
		if(this.area.getId()==null){
			this.getAreaService().saveArea(this.area);
		}else{
			this.getAreaService().renameArea(this.area);
		}
		this.areaTrees = this.getAreaService().getAreaTrees();
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
		storeDataModel = new PageListDataModel<Store>(25) {
			@Override
			public PageList<Store> fetchPage(int page, int pageSize) {
				return getStoreService().findStoreByOwner(null, 1, pageSize);
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
							page, pageSize);
				} else {
					return new EmptyPageList<Community>(page, pageSize);
				}
			}
		};
	}

	public void addCommunity() {
		this.community = new Community();
		this.community.setArea(this.selectedArea);
		if(this.selectedArea.getLevel()==2){
			this.community.setCity(this.selectedArea);
		}else{
			this.community.setCity(this.selectedArea.getParent());
		}
		
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
		this.refresh();
	}

	public void deleteCommunity(Community community) {
		this.getAreaService().deleteCommunity(community);
		this.refresh();
	}

	public void editAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		this.announcementAndNotice = this.getAnnouncementAndNoticeService().findById(announcementAndNotice.getId());
		this.announcementAndNoticeContents = this.announcementAndNotice.getContents();
	}
	
	public void deleteAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		this.getAnnouncementAndNoticeService().deleteAnnouncementAndNotice(
				announcementAndNotice);
		this.announcementAndNotices = this.getAnnouncementAndNoticeService()
		.findAnnouncementAndNotice(community, this.announcement);
	}
	
	public void addContentForAnnoAndNotice(){
		AnnouncementAndNoticeContent content = new AnnouncementAndNoticeContent();
		content.setText(true);
		this.announcementAndNoticeContents.add(content);
	}
	
	public void removeAnnouncementAndNoticeContent(AnnouncementAndNoticeContent content){
		this.announcementAndNoticeContents.remove(content);
	}
	
	public synchronized void uploadImage(UploadEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("upload image for store");
		}
		List<UploadItem> itemList = event.getUploadItems();
		UploadItem item = itemList.get(0);
		File imageTempFile = item.getFile();
		try {
			byte[] data = FileUtils.readFileToByteArray(imageTempFile);
			String suffix = org.apache.commons.lang.StringUtils
					.substringAfterLast(item.getFileName(), ".");
			String filePath = com.chinatelecom.xysq.util.StringUtils.generateFilePathByDate()
					+ UUID.randomUUID().toString() + "." + suffix;
			FileUtils.writeByteArrayToFile(new File(this.getSpringProperty()
					.getImageUrlPrefix() + "/" + filePath), data);
			Image image = new Image();
			image.setPath(filePath);
			image.setName(item.getFileName());
			AnnouncementAndNoticeContent content = new AnnouncementAndNoticeContent();
			content.setText(false);
			content.setImage(image);
			this.announcementAndNoticeContents.add(content);
		} catch (IOException e) {
			log.error("", e);
		}
	}

	public void publishAnnouncement() {
		if (this.getDefaultDataModel().getSelectedIds().size() == 0) {
			this.statusMessages.addToControl("communityTablePanel",
					Severity.ERROR, "请先选择要发布公告的小区");
			return;
		}
		this.announcementAndNotice = new AnnouncementAndNotice();
		this.announcementAndNotice.setInnerModule(InnerModule.ANNOUNCEMENT);
		this.announcementAndNotice.setSubmitter(this.getIdentity().getUser());
		this.announcementAndNoticeContents = new ArrayList<AnnouncementAndNoticeContent>();
		this.announcementAndNotice.setContents(this.announcementAndNoticeContents);
		this.announcement = true;
	}

	public void publishNotice() {
		if (this.getDefaultDataModel().getSelectedIds().size() == 0) {
			this.statusMessages.addToControl("communityTablePanel",
					Severity.ERROR, "请先选择要发布业主须知的小区");
			return;
		}
		this.announcementAndNotice = new AnnouncementAndNotice();
		this.announcementAndNotice.setInnerModule(InnerModule.NOTICE);
		this.announcementAndNotice.setSubmitter(this.getIdentity().getUser());
		this.announcementAndNoticeContents = new ArrayList<AnnouncementAndNoticeContent>();
		this.announcementAndNotice.setContents(this.announcementAndNoticeContents);
		this.announcement = false;
	}

	public void saveAnnouncementAndNotice() {
		if (announcementAndNotice.getId() == null) {
			this.getAnnouncementAndNoticeService()
					.publishAnnouncementAndNotice(announcementAndNotice,
							this.getDefaultDataModel().getSelectedIds());
		} else {
			this.getAnnouncementAndNoticeService().saveAnnouncementAndNotice(
					announcementAndNotice);
		}
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

	public void listAnnouncement(Community community) {
		this.announcement = true;
		this.community = community;
		this.announcementAndNotices = this.getAnnouncementAndNoticeService()
				.findAnnouncementAndNotice(community, this.announcement);
	}

	public void listNotice(Community community) {
		this.announcement = false;
		this.community = community;
		this.announcementAndNotices = this.getAnnouncementAndNoticeService()
				.findAnnouncementAndNotice(community, this.announcement);
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

	public boolean isAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(boolean announcement) {
		this.announcement = announcement;
	}

	public List<AnnouncementAndNotice> getAnnouncementAndNotices() {
		return announcementAndNotices;
	}

	public void setAnnouncementAndNotices(
			List<AnnouncementAndNotice> announcementAndNotices) {
		this.announcementAndNotices = announcementAndNotices;
	}

	public AnnouncementAndNotice getAnnouncementAndNotice() {
		return announcementAndNotice;
	}

	public void setAnnouncementAndNotice(
			AnnouncementAndNotice announcementAndNotice) {
		this.announcementAndNotice = announcementAndNotice;
	}

	public List<NoticeCategory> getNoticeCategories() {
		return noticeCategories;
	}

	public void setNoticeCategories(List<NoticeCategory> noticeCategories) {
		this.noticeCategories = noticeCategories;
	}

	public Area getSelectedArea() {
		return selectedArea;
	}

	public void setSelectedArea(Area selectedArea) {
		this.selectedArea = selectedArea;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<AnnouncementAndNoticeContent> getAnnouncementAndNoticeContents() {
		return announcementAndNoticeContents;
	}

	public void setAnnouncementAndNoticeContents(
			List<AnnouncementAndNoticeContent> announcementAndNoticeContents) {
		this.announcementAndNoticeContents = announcementAndNoticeContents;
	}

}
