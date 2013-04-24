package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 运输费用
 *
 * @author auto create
 * @since 1.0, null
 */
public class TransportCharge extends TaobaoObject {

	private static final long serialVersionUID = 5465983952327557687L;

	/**
	 * 总运费
	 */
	@ApiField("cost")
	private String cost;

	/**
	 * 计费方式。by_weight:按重量；by_volume:按体积。
	 */
	@ApiField("cost_by")
	private String costBy;

	/**
	 * 运费原价
	 */
	@ApiField("original_cost")
	private String originalCost;

	/**
	 * 节省费用
	 */
	@ApiField("saved_cost")
	private String savedCost;

	public String getCost() {
		return this.cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getCostBy() {
		return this.costBy;
	}
	public void setCostBy(String costBy) {
		this.costBy = costBy;
	}

	public String getOriginalCost() {
		return this.originalCost;
	}
	public void setOriginalCost(String originalCost) {
		this.originalCost = originalCost;
	}

	public String getSavedCost() {
		return this.savedCost;
	}
	public void setSavedCost(String savedCost) {
		this.savedCost = savedCost;
	}

}
