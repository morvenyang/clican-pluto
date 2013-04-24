package com.taobao.api.domain;

import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 线路的相关公司信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class FreightCompany extends TaobaoObject {

	private static final long serialVersionUID = 1565652756596838829L;

	/**
	 * 物流公司描述
	 */
	@ApiField("comments")
	private String comments;

	/**
	 * 公司id
	 */
	@ApiField("company_id")
	private Long companyId;

	/**
	 * 物流公司名
	 */
	@ApiField("company_name")
	private String companyName;

	/**
	 * 物流公司code标识
	 */
	@ApiField("companye_code")
	private String companyeCode;

	/**
	 * 公司级别。auth：认证；brand：品牌；noAuth：未认证；normal：普通
	 */
	@ApiField("corp_level")
	private String corpLevel;

	/**
	 * 物流公司客服电话
	 */
	@ApiField("customer_service_tel")
	private String customerServiceTel;

	/**
	 * 物流公司logo url
	 */
	@ApiField("logo_url")
	private String logoUrl;

	/**
	 * 物流公司店铺地址
	 */
	@ApiField("shop_url")
	private String shopUrl;

	/**
	 * 物流公司排序值
	 */
	@ApiField("sort")
	private Long sort;

	/**
	 * 公司增值服务说明链接
	 */
	@ApiField("vas_fee_help_url")
	private String vasFeeHelpUrl;

	/**
	 * 旺旺列表
	 */
	@ApiListField("wangwang_list")
	@ApiField("wangwang_info")
	private List<WangwangInfo> wangwangList;

	public String getComments() {
		return this.comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getCompanyId() {
		return this.companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return this.companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyeCode() {
		return this.companyeCode;
	}
	public void setCompanyeCode(String companyeCode) {
		this.companyeCode = companyeCode;
	}

	public String getCorpLevel() {
		return this.corpLevel;
	}
	public void setCorpLevel(String corpLevel) {
		this.corpLevel = corpLevel;
	}

	public String getCustomerServiceTel() {
		return this.customerServiceTel;
	}
	public void setCustomerServiceTel(String customerServiceTel) {
		this.customerServiceTel = customerServiceTel;
	}

	public String getLogoUrl() {
		return this.logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getShopUrl() {
		return this.shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public Long getSort() {
		return this.sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getVasFeeHelpUrl() {
		return this.vasFeeHelpUrl;
	}
	public void setVasFeeHelpUrl(String vasFeeHelpUrl) {
		this.vasFeeHelpUrl = vasFeeHelpUrl;
	}

	public List<WangwangInfo> getWangwangList() {
		return this.wangwangList;
	}
	public void setWangwangList(List<WangwangInfo> wangwangList) {
		this.wangwangList = wangwangList;
	}

}
