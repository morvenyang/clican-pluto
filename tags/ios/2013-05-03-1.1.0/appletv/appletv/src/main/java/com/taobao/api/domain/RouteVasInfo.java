package com.taobao.api.domain;

import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 线路的增值服务信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class RouteVasInfo extends TaobaoObject {

	private static final long serialVersionUID = 3199368478344633215L;

	/**
	 * 线路code标志
	 */
	@ApiField("route_code")
	private String routeCode;

	/**
	 * 增值服务列表
	 */
	@ApiListField("vas_list")
	@ApiField("logistics_vas")
	private List<LogisticsVas> vasList;

	public String getRouteCode() {
		return this.routeCode;
	}
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public List<LogisticsVas> getVasList() {
		return this.vasList;
	}
	public void setVasList(List<LogisticsVas> vasList) {
		this.vasList = vasList;
	}

}
