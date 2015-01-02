package com.chinatelecom.xysq.action;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.security.Restrict;

import com.chinatelecom.xysq.model.Area;
import com.chinatelecom.xysq.model.Community;

@Scope(ScopeType.PAGE)
@Name("areaAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class AreaAction extends BaseAction {

	private List<Area> areaTrees;

	private List<Community> communitiesBySelectedNode;

	public void listAreaTrees() {
		this.page = 1;
		this.areaTrees = this.getAreaService().getAreaTrees();
		if (this.areaTrees.size() != 0) {
			this.communitiesBySelectedNode=this.getAreaService().findCommunityByArea(this.areaTrees.get(0),
					page, PAGE_SIZE);
		} else {
			communitiesBySelectedNode = new ArrayList<Community>();
		}
	}

	@BypassInterceptors
	public List<Area> getAreaTrees() {
		return areaTrees;
	}

	public void setAreaTrees(List<Area> areaTrees) {
		this.areaTrees = areaTrees;
	}

	public List<Community> getCommunitiesBySelectedNode() {
		return communitiesBySelectedNode;
	}

	public void setCommunitiesBySelectedNode(
			List<Community> communitiesBySelectedNode) {
		this.communitiesBySelectedNode = communitiesBySelectedNode;
	}

}
