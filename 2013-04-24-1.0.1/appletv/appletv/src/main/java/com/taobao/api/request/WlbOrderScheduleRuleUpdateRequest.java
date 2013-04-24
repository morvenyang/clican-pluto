package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.WlbOrderScheduleRuleUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.wlb.order.schedule.rule.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class WlbOrderScheduleRuleUpdateRequest implements TaobaoRequest<WlbOrderScheduleRuleUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 备用发货仓库id
	 */
	private Long backupStoreId;

	/** 
	* 默认发货仓库id
	 */
	private Long defaultStoreId;

	/** 
	* 订单调度规则的额外规则设置： REMARK_STOP--有订单留言不自动下发 COD_STOP--货到付款订单不自动下发 CHECK_WAREHOUSE_DELIVER--检查仓库的配送范围
	 */
	private String option;

	/** 
	* 国家地区标准编码列表
	 */
	private String provAreaIds;

	/** 
	* 要修改的订单调度规则明细id
	 */
	private Long scheduleRuleId;

	public void setBackupStoreId(Long backupStoreId) {
		this.backupStoreId = backupStoreId;
	}
	public Long getBackupStoreId() {
		return this.backupStoreId;
	}

	public void setDefaultStoreId(Long defaultStoreId) {
		this.defaultStoreId = defaultStoreId;
	}
	public Long getDefaultStoreId() {
		return this.defaultStoreId;
	}

	public void setOption(String option) {
		this.option = option;
	}
	public String getOption() {
		return this.option;
	}

	public void setProvAreaIds(String provAreaIds) {
		this.provAreaIds = provAreaIds;
	}
	public String getProvAreaIds() {
		return this.provAreaIds;
	}

	public void setScheduleRuleId(Long scheduleRuleId) {
		this.scheduleRuleId = scheduleRuleId;
	}
	public Long getScheduleRuleId() {
		return this.scheduleRuleId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.wlb.order.schedule.rule.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("backup_store_id", this.backupStoreId);
		txtParams.put("default_store_id", this.defaultStoreId);
		txtParams.put("option", this.option);
		txtParams.put("prov_area_ids", this.provAreaIds);
		txtParams.put("schedule_rule_id", this.scheduleRuleId);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new TaobaoHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<WlbOrderScheduleRuleUpdateResponse> getResponseClass() {
		return WlbOrderScheduleRuleUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(scheduleRuleId,"scheduleRuleId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
