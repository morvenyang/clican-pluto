package com.taobao.api.domain;

import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 查询线路的结果
 *
 * @author auto create
 * @since 1.0, null
 */
public class QueryRouteResult extends TaobaoObject {

	private static final long serialVersionUID = 3518793835334119264L;

	/**
	 * 公司的线路统计信息。如德邦有几条线路。
	 */
	@ApiListField("company_route_summarys")
	@ApiField("company_route_summary")
	private List<CompanyRouteSummary> companyRouteSummarys;

	/**
	 * 是否上翻
	 */
	@ApiField("is_turn_level")
	private Boolean isTurnLevel;

	/**
	 * 线路详情
	 */
	@ApiField("pages_route_details")
	private RouteAlpPage pagesRouteDetails;

	/**
	 * 增值服务。
	 */
	@ApiListField("route_vas")
	@ApiField("route_vas_info")
	private List<RouteVasInfo> routeVas;

	/**
	 * 总公司数
	 */
	@ApiField("total_corps")
	private Long totalCorps;

	/**
	 * 总线路数
	 */
	@ApiField("total_routes")
	private Long totalRoutes;

	public List<CompanyRouteSummary> getCompanyRouteSummarys() {
		return this.companyRouteSummarys;
	}
	public void setCompanyRouteSummarys(List<CompanyRouteSummary> companyRouteSummarys) {
		this.companyRouteSummarys = companyRouteSummarys;
	}

	public Boolean getIsTurnLevel() {
		return this.isTurnLevel;
	}
	public void setIsTurnLevel(Boolean isTurnLevel) {
		this.isTurnLevel = isTurnLevel;
	}

	public RouteAlpPage getPagesRouteDetails() {
		return this.pagesRouteDetails;
	}
	public void setPagesRouteDetails(RouteAlpPage pagesRouteDetails) {
		this.pagesRouteDetails = pagesRouteDetails;
	}

	public List<RouteVasInfo> getRouteVas() {
		return this.routeVas;
	}
	public void setRouteVas(List<RouteVasInfo> routeVas) {
		this.routeVas = routeVas;
	}

	public Long getTotalCorps() {
		return this.totalCorps;
	}
	public void setTotalCorps(Long totalCorps) {
		this.totalCorps = totalCorps;
	}

	public Long getTotalRoutes() {
		return this.totalRoutes;
	}
	public void setTotalRoutes(Long totalRoutes) {
		this.totalRoutes = totalRoutes;
	}

}
