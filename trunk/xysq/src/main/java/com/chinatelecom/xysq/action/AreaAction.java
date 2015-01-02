package com.chinatelecom.xysq.action;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.security.Restrict;

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
			area  = this.areaTrees.get(0);
			
		} else {
			area = null;
		}
		communitiesBySelectedNode = new PageListDataModel<Community>(
				this.getPageSize()) {
			@Override
			public PageList<Community> fetchPage(int page, int pageSize) {
				if(area!=null){
					return  getAreaService().findCommunityByArea(
							area, page, PAGE_SIZE);
				}else{
					return new EmptyPageList<Community>(page,PAGE_SIZE);
				}
			}
		};
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
