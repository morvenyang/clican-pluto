package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 线路的整条完整信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class ComplexLogisticsRoute extends TaobaoObject {

	private static final long serialVersionUID = 7645816136397544494L;

	/**
	 * 线路运输相关的信息
	 */
	@ApiField("carriage_info")
	private RouteCarriageInfo carriageInfo;

	/**
	 * 物流公司信息
	 */
	@ApiField("company")
	private FreightCompany company;

	/**
	 * 线路扩展信息
	 */
	@ApiField("extenal_info")
	private RouteExtenalInfo extenalInfo;

	/**
	 * 始发地area id
	 */
	@ApiField("from_area_id")
	private Long fromAreaId;

	/**
	 * 始发城市名
	 */
	@ApiField("from_city_name")
	private String fromCityName;

	/**
	 * 始发区
	 */
	@ApiField("from_county_name")
	private String fromCountyName;

	/**
	 * 始发省
	 */
	@ApiField("from_province_name")
	private String fromProvinceName;

	/**
	 * 促销相关信息
	 */
	@ApiField("promotion_info")
	private RoutePromotionInfo promotionInfo;

	/**
	 * 线路code标识
	 */
	@ApiField("route_code")
	private String routeCode;

	/**
	 * 线路上相关的统计信息
	 */
	@ApiField("statistics_info")
	private RouteStatisticsInfo statisticsInfo;

	/**
	 * 目的地area id
	 */
	@ApiField("to_area_id")
	private Long toAreaId;

	/**
	 * 目的地城市
	 */
	@ApiField("to_city_name")
	private String toCityName;

	/**
	 * 目的地区
	 */
	@ApiField("to_county_name")
	private String toCountyName;

	/**
	 * 目的地省
	 */
	@ApiField("to_province_name")
	private String toProvinceName;

	public RouteCarriageInfo getCarriageInfo() {
		return this.carriageInfo;
	}
	public void setCarriageInfo(RouteCarriageInfo carriageInfo) {
		this.carriageInfo = carriageInfo;
	}

	public FreightCompany getCompany() {
		return this.company;
	}
	public void setCompany(FreightCompany company) {
		this.company = company;
	}

	public RouteExtenalInfo getExtenalInfo() {
		return this.extenalInfo;
	}
	public void setExtenalInfo(RouteExtenalInfo extenalInfo) {
		this.extenalInfo = extenalInfo;
	}

	public Long getFromAreaId() {
		return this.fromAreaId;
	}
	public void setFromAreaId(Long fromAreaId) {
		this.fromAreaId = fromAreaId;
	}

	public String getFromCityName() {
		return this.fromCityName;
	}
	public void setFromCityName(String fromCityName) {
		this.fromCityName = fromCityName;
	}

	public String getFromCountyName() {
		return this.fromCountyName;
	}
	public void setFromCountyName(String fromCountyName) {
		this.fromCountyName = fromCountyName;
	}

	public String getFromProvinceName() {
		return this.fromProvinceName;
	}
	public void setFromProvinceName(String fromProvinceName) {
		this.fromProvinceName = fromProvinceName;
	}

	public RoutePromotionInfo getPromotionInfo() {
		return this.promotionInfo;
	}
	public void setPromotionInfo(RoutePromotionInfo promotionInfo) {
		this.promotionInfo = promotionInfo;
	}

	public String getRouteCode() {
		return this.routeCode;
	}
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public RouteStatisticsInfo getStatisticsInfo() {
		return this.statisticsInfo;
	}
	public void setStatisticsInfo(RouteStatisticsInfo statisticsInfo) {
		this.statisticsInfo = statisticsInfo;
	}

	public Long getToAreaId() {
		return this.toAreaId;
	}
	public void setToAreaId(Long toAreaId) {
		this.toAreaId = toAreaId;
	}

	public String getToCityName() {
		return this.toCityName;
	}
	public void setToCityName(String toCityName) {
		this.toCityName = toCityName;
	}

	public String getToCountyName() {
		return this.toCountyName;
	}
	public void setToCountyName(String toCountyName) {
		this.toCountyName = toCountyName;
	}

	public String getToProvinceName() {
		return this.toProvinceName;
	}
	public void setToProvinceName(String toProvinceName) {
		this.toProvinceName = toProvinceName;
	}

}
