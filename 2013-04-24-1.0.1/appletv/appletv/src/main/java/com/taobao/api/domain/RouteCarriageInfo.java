package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 线路运输相关的基本信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class RouteCarriageInfo extends TaobaoObject {

	private static final long serialVersionUID = 1599749336475293459L;

	/**
	 * 续重
	 */
	@ApiField("add_fee")
	private String addFee;

	/**
	 * 线路的备注
	 */
	@ApiField("comments")
	private String comments;

	/**
	 * 送货时效
	 */
	@ApiField("give_time")
	private String giveTime;

	/**
	 * 首重
	 */
	@ApiField("initial_fee")
	private String initialFee;

	/**
	 * 保底消费
	 */
	@ApiField("least_expense")
	private String leastExpense;

	/**
	 * 轻货原价（按体积计费）
	 */
	@ApiField("orig_volume_rate")
	private String origVolumeRate;

	/**
	 * 重物原价
	 */
	@ApiField("orig_weight_rate")
	private String origWeightRate;

	/**
	 * 价格描述
	 */
	@ApiField("price_description")
	private String priceDescription;

	/**
	 * 提货时间
	 */
	@ApiField("take_time")
	private String takeTime;

	/**
	 * 运价模式。D2D:门到门；S2S：站到站；D2S：门到站；S2D：站到门。
	 */
	@ApiField("transport_mode")
	private String transportMode;

	/**
	 * 运输方式名称
	 */
	@ApiField("transport_name")
	private String transportName;

	/**
	 * 运输时效的文字描述
	 */
	@ApiField("transport_time")
	private String transportTime;

	/**
	 * 运输时效的小时数。可用于排序。
	 */
	@ApiField("transport_time_hours")
	private Long transportTimeHours;

	/**
	 * 运输方式code标识
	 */
	@ApiField("transport_type_code")
	private String transportTypeCode;

	/**
	 * 运输方式。QC：汽运；HK:航空。
	 */
	@ApiField("transport_way")
	private String transportWay;

	/**
	 * 轻货价格（按体积计费）
	 */
	@ApiField("volume_rate")
	private String volumeRate;

	/**
	 * 重物价格
	 */
	@ApiField("weight_rate")
	private String weightRate;

	public String getAddFee() {
		return this.addFee;
	}
	public void setAddFee(String addFee) {
		this.addFee = addFee;
	}

	public String getComments() {
		return this.comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getGiveTime() {
		return this.giveTime;
	}
	public void setGiveTime(String giveTime) {
		this.giveTime = giveTime;
	}

	public String getInitialFee() {
		return this.initialFee;
	}
	public void setInitialFee(String initialFee) {
		this.initialFee = initialFee;
	}

	public String getLeastExpense() {
		return this.leastExpense;
	}
	public void setLeastExpense(String leastExpense) {
		this.leastExpense = leastExpense;
	}

	public String getOrigVolumeRate() {
		return this.origVolumeRate;
	}
	public void setOrigVolumeRate(String origVolumeRate) {
		this.origVolumeRate = origVolumeRate;
	}

	public String getOrigWeightRate() {
		return this.origWeightRate;
	}
	public void setOrigWeightRate(String origWeightRate) {
		this.origWeightRate = origWeightRate;
	}

	public String getPriceDescription() {
		return this.priceDescription;
	}
	public void setPriceDescription(String priceDescription) {
		this.priceDescription = priceDescription;
	}

	public String getTakeTime() {
		return this.takeTime;
	}
	public void setTakeTime(String takeTime) {
		this.takeTime = takeTime;
	}

	public String getTransportMode() {
		return this.transportMode;
	}
	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}

	public String getTransportName() {
		return this.transportName;
	}
	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getTransportTime() {
		return this.transportTime;
	}
	public void setTransportTime(String transportTime) {
		this.transportTime = transportTime;
	}

	public Long getTransportTimeHours() {
		return this.transportTimeHours;
	}
	public void setTransportTimeHours(Long transportTimeHours) {
		this.transportTimeHours = transportTimeHours;
	}

	public String getTransportTypeCode() {
		return this.transportTypeCode;
	}
	public void setTransportTypeCode(String transportTypeCode) {
		this.transportTypeCode = transportTypeCode;
	}

	public String getTransportWay() {
		return this.transportWay;
	}
	public void setTransportWay(String transportWay) {
		this.transportWay = transportWay;
	}

	public String getVolumeRate() {
		return this.volumeRate;
	}
	public void setVolumeRate(String volumeRate) {
		this.volumeRate = volumeRate;
	}

	public String getWeightRate() {
		return this.weightRate;
	}
	public void setWeightRate(String weightRate) {
		this.weightRate = weightRate;
	}

}
