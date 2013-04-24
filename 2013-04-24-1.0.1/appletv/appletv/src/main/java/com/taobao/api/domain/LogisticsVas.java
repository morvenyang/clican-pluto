package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 货运增值服务信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class LogisticsVas extends TaobaoObject {

	private static final long serialVersionUID = 5563843795529294512L;

	/**
	 * 计费类型。0：计费；1：面议；2：免费
	 */
	@ApiField("charge_calculate_type")
	private String chargeCalculateType;

	/**
	 * 增值服务的描述
	 */
	@ApiField("comments")
	private String comments;

	/**
	 * 默认是否选中
	 */
	@ApiField("default_selected")
	private Boolean defaultSelected;

	/**
	 * 是否需要展示
	 */
	@ApiField("displayable")
	private Boolean displayable;

	/**
	 * 是否必选
	 */
	@ApiField("needed")
	private Boolean needed;

	/**
	 * 是否需要输入框，输入相应的金额。如报价金额。
	 */
	@ApiField("value_displayable")
	private Boolean valueDisplayable;

	/**
	 * 增值服务的code
	 */
	@ApiField("vas_code")
	private String vasCode;

	/**
	 * 增值服务名
	 */
	@ApiField("vas_name")
	private String vasName;

	public String getChargeCalculateType() {
		return this.chargeCalculateType;
	}
	public void setChargeCalculateType(String chargeCalculateType) {
		this.chargeCalculateType = chargeCalculateType;
	}

	public String getComments() {
		return this.comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getDefaultSelected() {
		return this.defaultSelected;
	}
	public void setDefaultSelected(Boolean defaultSelected) {
		this.defaultSelected = defaultSelected;
	}

	public Boolean getDisplayable() {
		return this.displayable;
	}
	public void setDisplayable(Boolean displayable) {
		this.displayable = displayable;
	}

	public Boolean getNeeded() {
		return this.needed;
	}
	public void setNeeded(Boolean needed) {
		this.needed = needed;
	}

	public Boolean getValueDisplayable() {
		return this.valueDisplayable;
	}
	public void setValueDisplayable(Boolean valueDisplayable) {
		this.valueDisplayable = valueDisplayable;
	}

	public String getVasCode() {
		return this.vasCode;
	}
	public void setVasCode(String vasCode) {
		this.vasCode = vasCode;
	}

	public String getVasName() {
		return this.vasName;
	}
	public void setVasName(String vasName) {
		this.vasName = vasName;
	}

}
