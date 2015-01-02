package com.chinatelecom.xysq.action;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.model.BroadbandRemind;

public class BroadbandRemindAction extends PageListAction<BroadbandRemind> {

	private PageListDataModel<BroadbandRemind> broadbandRemindDataModel;

	public void listBroadbandReminds() {
		broadbandRemindDataModel = new PageListDataModel<BroadbandRemind>(
				this.getPageSize()) {
			@Override
			public PageList<BroadbandRemind> fetchPage(int page, int pageSize) {
				return getBroadbandRemindService().findBroadbandRemind(page,
						pageSize);
			}
		};
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
