package com.taobao.api.domain;

import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 增值服务的总的费用
 *
 * @author auto create
 * @since 1.0, null
 */
public class LogisticsVasCharge extends TaobaoObject {

	private static final long serialVersionUID = 7481765349457867862L;

	/**
	 * 原始总费用
	 */
	@ApiField("original_total_vas_cost")
	private String originalTotalVasCost;

	/**
	 * 总费用
	 */
	@ApiField("total_vas_cost")
	private String totalVasCost;

	/**
	 * 节省的费用。即原始费用-现在费用
	 */
	@ApiField("total_vas_save_cost")
	private String totalVasSaveCost;

	/**
	 * 增值服务每项费用
	 */
	@ApiListField("vas_cost_list")
	@ApiField("logistics_vas_item_charge")
	private List<LogisticsVasItemCharge> vasCostList;

	public String getOriginalTotalVasCost() {
		return this.originalTotalVasCost;
	}
	public void setOriginalTotalVasCost(String originalTotalVasCost) {
		this.originalTotalVasCost = originalTotalVasCost;
	}

	public String getTotalVasCost() {
		return this.totalVasCost;
	}
	public void setTotalVasCost(String totalVasCost) {
		this.totalVasCost = totalVasCost;
	}

	public String getTotalVasSaveCost() {
		return this.totalVasSaveCost;
	}
	public void setTotalVasSaveCost(String totalVasSaveCost) {
		this.totalVasSaveCost = totalVasSaveCost;
	}

	public List<LogisticsVasItemCharge> getVasCostList() {
		return this.vasCostList;
	}
	public void setVasCostList(List<LogisticsVasItemCharge> vasCostList) {
		this.vasCostList = vasCostList;
	}

}
