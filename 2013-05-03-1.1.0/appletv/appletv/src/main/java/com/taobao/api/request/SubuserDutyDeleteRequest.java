package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SubuserDutyDeleteResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.subuser.duty.delete request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SubuserDutyDeleteRequest implements TaobaoRequest<SubuserDutyDeleteResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 职务ID
	 */
	private Long dutyId;

	/** 
	* 主账号用户名
	 */
	private String userNick;

	public void setDutyId(Long dutyId) {
		this.dutyId = dutyId;
	}
	public Long getDutyId() {
		return this.dutyId;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	public String getUserNick() {
		return this.userNick;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.subuser.duty.delete";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("duty_id", this.dutyId);
		txtParams.put("user_nick", this.userNick);
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

	public Class<SubuserDutyDeleteResponse> getResponseClass() {
		return SubuserDutyDeleteResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(dutyId,"dutyId");
		RequestCheckUtils.checkNotEmpty(userNick,"userNick");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
