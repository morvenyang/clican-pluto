package com.taobao.api.domain;

import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 线路附加信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class RouteExtenalInfo extends TaobaoObject {

	private static final long serialVersionUID = 5581694746977784548L;

	/**
	 * 线路是否支持代收货款
	 */
	@ApiField("cod")
	private Boolean cod;

	/**
	 * 是否缴纳保证金
	 */
	@ApiField("credit_opened")
	private Boolean creditOpened;

	/**
	 * 缴纳保证金额
	 */
	@ApiField("credit_total_balance")
	private String creditTotalBalance;

	/**
	 * 线路为推荐线路
	 */
	@ApiField("recommend")
	private Boolean recommend;

	/**
	 * 支持的保障服务类型列表。JGZS：价格真实；SXBZ：时效保障；KSSL：快速手里。
	 */
	@ApiListField("special_codes")
	@ApiField("string")
	private List<String> specialCodes;

	/**
	 * 是否置顶
	 */
	@ApiField("top")
	private Boolean top;

	public Boolean getCod() {
		return this.cod;
	}
	public void setCod(Boolean cod) {
		this.cod = cod;
	}

	public Boolean getCreditOpened() {
		return this.creditOpened;
	}
	public void setCreditOpened(Boolean creditOpened) {
		this.creditOpened = creditOpened;
	}

	public String getCreditTotalBalance() {
		return this.creditTotalBalance;
	}
	public void setCreditTotalBalance(String creditTotalBalance) {
		this.creditTotalBalance = creditTotalBalance;
	}

	public Boolean getRecommend() {
		return this.recommend;
	}
	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	public List<String> getSpecialCodes() {
		return this.specialCodes;
	}
	public void setSpecialCodes(List<String> specialCodes) {
		this.specialCodes = specialCodes;
	}

	public Boolean getTop() {
		return this.top;
	}
	public void setTop(Boolean top) {
		this.top = top;
	}

}
