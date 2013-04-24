package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 淘宝机票政策详情信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class PolicyDetail extends TaobaoObject {

	private static final long serialVersionUID = 5382785719565682462L;

	/**
	 * 改签规则
	 */
	@ApiField("change_rule")
	private String changeRule;

	/**
	 * 当前政策仅支持一周的中的某些天，如1-7分别表示周一到周日
	 */
	@ApiField("day_of_weeks")
	private String dayOfWeeks;

	/**
	 * ei项
	 */
	@ApiField("ei")
	private String ei;

	/**
	 * 当前政策排除的日期，录入日期将不在搜索结果页展现
	 */
	@ApiField("exclude_date")
	private String excludeDate;

	/**
	 * 设置当前政策的备注信息
	 */
	@ApiField("memo")
	private String memo;

	/**
	 * officeId，长度限制6位(根据航信政策可能调整)
	 */
	@ApiField("office_id")
	private String officeId;

	/**
	 * 退票规则
	 */
	@ApiField("refund_rule")
	private String refundRule;

	/**
	 * 签转规则
	 */
	@ApiField("reissue_rule")
	private String reissueRule;

	/**
	 * 特殊说明
	 */
	@ApiField("special_rule")
	private String specialRule;

	public String getChangeRule() {
		return this.changeRule;
	}
	public void setChangeRule(String changeRule) {
		this.changeRule = changeRule;
	}

	public String getDayOfWeeks() {
		return this.dayOfWeeks;
	}
	public void setDayOfWeeks(String dayOfWeeks) {
		this.dayOfWeeks = dayOfWeeks;
	}

	public String getEi() {
		return this.ei;
	}
	public void setEi(String ei) {
		this.ei = ei;
	}

	public String getExcludeDate() {
		return this.excludeDate;
	}
	public void setExcludeDate(String excludeDate) {
		this.excludeDate = excludeDate;
	}

	public String getMemo() {
		return this.memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOfficeId() {
		return this.officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getRefundRule() {
		return this.refundRule;
	}
	public void setRefundRule(String refundRule) {
		this.refundRule = refundRule;
	}

	public String getReissueRule() {
		return this.reissueRule;
	}
	public void setReissueRule(String reissueRule) {
		this.reissueRule = reissueRule;
	}

	public String getSpecialRule() {
		return this.specialRule;
	}
	public void setSpecialRule(String specialRule) {
		this.specialRule = specialRule;
	}

}
