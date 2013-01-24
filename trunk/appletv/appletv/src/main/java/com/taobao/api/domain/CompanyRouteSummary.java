package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 公司的线路统计
 *
 * @author auto create
 * @since 1.0, null
 */
public class CompanyRouteSummary extends TaobaoObject {

	private static final long serialVersionUID = 1311565748655248883L;

	/**
	 * 物流公司code
	 */
	@ApiField("company_code")
	private String companyCode;

	/**
	 * 物流公司id
	 */
	@ApiField("company_id")
	private String companyId;

	/**
	 * 物流公司名
	 */
	@ApiField("company_name")
	private String companyName;

	/**
	 * 公司的线路数目
	 */
	@ApiField("route_counts")
	private Long routeCounts;

	public String getCompanyCode() {
		return this.companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyId() {
		return this.companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return this.companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Long getRouteCounts() {
		return this.routeCounts;
	}
	public void setRouteCounts(Long routeCounts) {
		this.routeCounts = routeCounts;
	}

}
