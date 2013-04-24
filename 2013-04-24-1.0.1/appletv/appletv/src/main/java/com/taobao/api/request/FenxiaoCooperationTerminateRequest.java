package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FenxiaoCooperationTerminateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.fenxiao.cooperation.terminate request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class FenxiaoCooperationTerminateRequest implements TaobaoRequest<FenxiaoCooperationTerminateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 合作编号
	 */
	private Long cooperateId;

	/** 
	* 终止天数，可选1,2,3,5,7,15，在多少天数内终止
	 */
	private Long endRemainDays;

	/** 
	* 终止说明（5-2000字）
	 */
	private String endRemark;

	public void setCooperateId(Long cooperateId) {
		this.cooperateId = cooperateId;
	}
	public Long getCooperateId() {
		return this.cooperateId;
	}

	public void setEndRemainDays(Long endRemainDays) {
		this.endRemainDays = endRemainDays;
	}
	public Long getEndRemainDays() {
		return this.endRemainDays;
	}

	public void setEndRemark(String endRemark) {
		this.endRemark = endRemark;
	}
	public String getEndRemark() {
		return this.endRemark;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.fenxiao.cooperation.terminate";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cooperate_id", this.cooperateId);
		txtParams.put("end_remain_days", this.endRemainDays);
		txtParams.put("end_remark", this.endRemark);
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

	public Class<FenxiaoCooperationTerminateResponse> getResponseClass() {
		return FenxiaoCooperationTerminateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(cooperateId,"cooperateId");
		RequestCheckUtils.checkNotEmpty(endRemainDays,"endRemainDays");
		RequestCheckUtils.checkNotEmpty(endRemark,"endRemark");
		RequestCheckUtils.checkMaxLength(endRemark,2000,"endRemark");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
