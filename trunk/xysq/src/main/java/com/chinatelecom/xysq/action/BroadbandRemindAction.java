package com.chinatelecom.xysq.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.model.BroadbandRemind;

@Scope(ScopeType.PAGE)
@Name("broadbandRemindAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class BroadbandRemindAction extends PageListAction<BroadbandRemind> {

	private PageListDataModel<BroadbandRemind> broadbandRemindDataModel;
	
	private List<BroadbandRemind> bbrList;

	public void listBroadbandReminds() {
		refresh();
	}
	
	private void refresh(){
		broadbandRemindDataModel = new PageListDataModel<BroadbandRemind>(
				this.getPageSize()) {
			@Override
			public PageList<BroadbandRemind> fetchPage(int page, int pageSize) {
				return getBroadbandRemindService().findBroadbandRemind(page,
						pageSize);
			}
		};
	}

	private String getCellString(Cell cell) {
		String value;
		if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
			value = ((long)cell.getNumericCellValue())+"";
		}else{
			value = cell.getStringCellValue();
		}
		if (StringUtils.isNotEmpty(value)) {
			return value.trim();
		} else {
			return value;
		}
	}

	public synchronized void saveImportExcel(){
		this.getBroadbandRemindService().saveBoradbandReminds(bbrList);
		bbrList.clear();
		refresh();
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
			int i = 0;
			bbrList = new ArrayList<BroadbandRemind>();
			while (true) {
				Row row = sheet.getRow(i);
				i++;
				if (row == null) {
					break;
				}
				String msisdn = this.getCellString(row.getCell(0));
				String broadBandId = this.getCellString(row.getCell(1));
				String userName = this.getCellString(row.getCell(2));
				Date expiredDate = row.getCell(3).getDateCellValue();
				if(StringUtils.isEmpty(msisdn)||expiredDate==null){
					continue;
				}
				BroadbandRemind bbr = new BroadbandRemind();
				bbr.setMsisdn(msisdn);
				bbr.setBroadBandId(broadBandId);
				bbr.setUserName(userName);
				bbr.setExpiredDate(expiredDate);
				bbrList.add(bbr);
			}
		} catch (Exception e) {
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

	public PageListDataModel<BroadbandRemind> getBroadbandRemindDataModel() {
		return broadbandRemindDataModel;
	}

	public void setBroadbandRemindDataModel(
			PageListDataModel<BroadbandRemind> broadbandRemindDataModel) {
		this.broadbandRemindDataModel = broadbandRemindDataModel;
	}

	@Override
	public PageListDataModel<BroadbandRemind> getDefaultDataModel() {
		return broadbandRemindDataModel;
	}

}
