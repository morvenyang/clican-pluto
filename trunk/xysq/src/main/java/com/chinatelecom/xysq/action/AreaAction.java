package com.chinatelecom.xysq.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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

	public void listAreaTrees() {
		this.page = 1;
		this.areaTrees = this.getAreaService().getAreaTrees();
		final Area area;
		if (this.areaTrees.size() != 0) {
			area = this.areaTrees.get(0);

		} else {
			area = null;
		}
		communitiesBySelectedNode = new PageListDataModel<Community>(
				this.getPageSize()) {
			@Override
			public PageList<Community> fetchPage(int page, int pageSize) {
				if (area != null) {
					return getAreaService().findCommunityByArea(area, page,
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
			while (true) {
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				String area1 = row.getCell(1).getStringCellValue();
				String area2 = row.getCell(2).getStringCellValue();
				String area3 = row.getCell(3).getStringCellValue();
				String community = row.getCell(4).getStringCellValue();

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
