package com.chinatelecom.xysq.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
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
import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;

@Scope(ScopeType.PAGE)
@Name("areaAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AreaAction extends PageListAction<Community> {

	private List<Area> areaTrees;

	private PageListDataModel<Community> communitiesBySelectedNode;
	
	private Area selectedArea;

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
	
	private void refresh(){
		communitiesBySelectedNode = new PageListDataModel<Community>(
				this.getPageSize()) {
			@Override
			public PageList<Community> fetchPage(int page, int pageSize) {
				if (selectedArea != null) {
					return getAreaService().findCommunityByArea(selectedArea, page,
							PAGE_SIZE);
				} else {
					return new EmptyPageList<Community>(page, PAGE_SIZE);
				}
			}
		};
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
			POIFSFileSystem fs = new POIFSFileSystem(fis);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			int i = 1;
			List<String> areaFullNames = new ArrayList<String>();
			Map<String, List<Community>> communityMap = new HashMap<String, List<Community>>();
			while (true) {
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				String area1 = row.getCell(1).getStringCellValue();
				if (StringUtils.isNotEmpty(area1)) {
					area1 = area1.trim();
				}

				String area2 = row.getCell(2).getStringCellValue();
				if (StringUtils.isNotEmpty(area2)) {
					area2 = area2.trim();
				}
				String area3 = row.getCell(3).getStringCellValue();
				if (StringUtils.isNotEmpty(area3)) {
					area3 = area3.trim();
				}
				String community = row.getCell(4).getStringCellValue();
				if (StringUtils.isNotEmpty(community)) {
					community = community.trim();
				}
				String detailAddress = row.getCell(5).getStringCellValue();
				if (StringUtils.isNotEmpty(detailAddress)) {
					detailAddress = detailAddress.trim();
				}
				areaFullNames.add(area1);
				String areaFullName = area1;
				if (StringUtils.isNotEmpty(area2)) {
					areaFullNames.add(area1 + "/" + area2);
					areaFullName = area1 + "/" + area2;
					if (StringUtils.isNotEmpty(area3)) {
						areaFullNames.add(area1 + "/" + area2 + "/" + area3);
						areaFullName = area1 + "/" + area2 + "/" + area3;
					}
				}
				if (StringUtils.isNotEmpty(community)) {
					if (!communityMap.containsKey(areaFullName)) {
						communityMap.put(areaFullName, new ArrayList<Community>());
					}
					List<Community> communityList = communityMap.get(areaFullName);
					Community c = new Community();
					c.setName(community);
					c.setDetailAddress(detailAddress);
					communityList.add(c);
				}
			}
			Map<String,Area> areaMap = this.getAreaService().mergeAreas(areaFullNames);
			getAreaService().mergeCommunities(communityMap,areaMap);
			refresh();
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

	@Override
	public PageListDataModel<Community> getDefaultDataModel() {
		return communitiesBySelectedNode;
	}

}
