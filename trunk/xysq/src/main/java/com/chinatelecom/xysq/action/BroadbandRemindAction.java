package com.chinatelecom.xysq.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import com.chinatelecom.xysq.bean.PageList;
import com.chinatelecom.xysq.bean.PageListDataModel;
import com.chinatelecom.xysq.model.BroadbandRemind;

@Scope(ScopeType.PAGE)
@Name("broadbandRemindAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
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
