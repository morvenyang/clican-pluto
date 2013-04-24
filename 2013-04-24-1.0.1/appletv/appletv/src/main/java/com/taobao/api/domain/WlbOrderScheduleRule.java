package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 订单调度规则
 *
 * @author auto create
 * @since 1.0, null
 */
public class WlbOrderScheduleRule extends TaobaoObject {

	private static final long serialVersionUID = 5529464488936523547L;

	/**
	 * 收货地址范围: 6位数的地址ID，用逗号分隔。如“140400,230500”。同一个卖家的订单调度规则中，设置的收货地址范围不能重复。
	 */
	@ApiField("area_ids")
	private String areaIds;

	/**
	 * 备用配送中心ID
	 */
	@ApiField("backup_store_id")
	private Long backupStoreId;

	/**
	 * 默认配送中心ID
	 */
	@ApiField("default_store_id")
	private Long defaultStoreId;

	/**
	 * 订单调度规则ID
	 */
	@ApiField("id")
	private Long id;

	/**
	 * 发货规则的额外规则设置： 允许组合，用逗号隔开.
REMARK_STOP--有订单留言不自动下发;
COD_STOP--货到付款订单不自动下发;
CHECK_WAREHOUSE_DELIVER--检查仓库的配送范围
	 */
	@ApiField("options")
	private String options;

	/**
	 * 预售配送中心ID（预留，暂未使用）
	 */
	@ApiField("presell_store_id")
	private Long presellStoreId;

	/**
	 * wlb_base_rule表的ID
	 */
	@ApiField("rule_id")
	private Long ruleId;

	/**
	 * 商家userId
	 */
	@ApiField("user_id")
	private Long userId;

	/**
	 * 商品userNick
	 */
	@ApiField("user_nick")
	private String userNick;

	public String getAreaIds() {
		return this.areaIds;
	}
	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public Long getBackupStoreId() {
		return this.backupStoreId;
	}
	public void setBackupStoreId(Long backupStoreId) {
		this.backupStoreId = backupStoreId;
	}

	public Long getDefaultStoreId() {
		return this.defaultStoreId;
	}
	public void setDefaultStoreId(Long defaultStoreId) {
		this.defaultStoreId = defaultStoreId;
	}

	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getOptions() {
		return this.options;
	}
	public void setOptions(String options) {
		this.options = options;
	}

	public Long getPresellStoreId() {
		return this.presellStoreId;
	}
	public void setPresellStoreId(Long presellStoreId) {
		this.presellStoreId = presellStoreId;
	}

	public Long getRuleId() {
		return this.ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserNick() {
		return this.userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

}
