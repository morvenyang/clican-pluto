package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 物流费用信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class OrderCharge extends TaobaoObject {

	private static final long serialVersionUID = 4742678583619566387L;

	/**
	 * 原总费用
	 */
	@ApiField("original_total_cost")
	private String originalTotalCost;

	/**
	 * 其他费用
	 */
	@ApiField("other_cost")
	private String otherCost;

	/**
	 * 总费用
	 */
	@ApiField("total_cost")
	private String totalCost;

	/**
	 * 节省费用
	 */
	@ApiField("total_saved_cost")
	private String totalSavedCost;

	/**
	 * 运输费用
	 */
	@ApiField("transport_charge")
	private TransportCharge transportCharge;

	/**
	 * 增值服务费用
	 */
	@ApiField("vas_charge")
	private LogisticsVasCharge vasCharge;

	public String getOriginalTotalCost() {
		return this.originalTotalCost;
	}
	public void setOriginalTotalCost(String originalTotalCost) {
		this.originalTotalCost = originalTotalCost;
	}

	public String getOtherCost() {
		return this.otherCost;
	}
	public void setOtherCost(String otherCost) {
		this.otherCost = otherCost;
	}

	public String getTotalCost() {
		return this.totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getTotalSavedCost() {
		return this.totalSavedCost;
	}
	public void setTotalSavedCost(String totalSavedCost) {
		this.totalSavedCost = totalSavedCost;
	}

	public TransportCharge getTransportCharge() {
		return this.transportCharge;
	}
	public void setTransportCharge(TransportCharge transportCharge) {
		this.transportCharge = transportCharge;
	}

	public LogisticsVasCharge getVasCharge() {
		return this.vasCharge;
	}
	public void setVasCharge(LogisticsVasCharge vasCharge) {
		this.vasCharge = vasCharge;
	}

}
