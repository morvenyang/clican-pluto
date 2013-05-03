package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 物流增值服务每项费用
 *
 * @author auto create
 * @since 1.0, null
 */
public class LogisticsVasItemCharge extends TaobaoObject {

	private static final long serialVersionUID = 8447677379566637495L;

	/**
	 * 真实价格（折后价）
	 */
	@ApiField("cost")
	private String cost;

	/**
	 * 原价
	 */
	@ApiField("original_cost")
	private String originalCost;

	/**
	 * 增值服务的code，为业务标识字段。
	 */
	@ApiField("vas_code")
	private String vasCode;

	/**
	 * 增值服务系统字段，一般以业务字段vas_code为标识。
	 */
	@ApiField("vas_id")
	private String vasId;

	public String getCost() {
		return this.cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getOriginalCost() {
		return this.originalCost;
	}
	public void setOriginalCost(String originalCost) {
		this.originalCost = originalCost;
	}

	public String getVasCode() {
		return this.vasCode;
	}
	public void setVasCode(String vasCode) {
		this.vasCode = vasCode;
	}

	public String getVasId() {
		return this.vasId;
	}
	public void setVasId(String vasId) {
		this.vasId = vasId;
	}

}
