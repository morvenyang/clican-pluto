package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SubuserInfoUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.subuser.info.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SubuserInfoUpdateRequest implements TaobaoRequest<SubuserInfoUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 是否停用子账号 true:表示停用该子账号false:表示开启该子账号
	 */
	private Boolean isDisableSubaccount;

	/** 
	* 子账号是否参与分流 true:参与分流 false:不参与分流
	 */
	private Boolean isDispatch;

	/** 
	* 子账号ID
	 */
	private Long subId;

	public void setIsDisableSubaccount(Boolean isDisableSubaccount) {
		this.isDisableSubaccount = isDisableSubaccount;
	}
	public Boolean getIsDisableSubaccount() {
		return this.isDisableSubaccount;
	}

	public void setIsDispatch(Boolean isDispatch) {
		this.isDispatch = isDispatch;
	}
	public Boolean getIsDispatch() {
		return this.isDispatch;
	}

	public void setSubId(Long subId) {
		this.subId = subId;
	}
	public Long getSubId() {
		return this.subId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.subuser.info.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("is_disable_subaccount", this.isDisableSubaccount);
		txtParams.put("is_dispatch", this.isDispatch);
		txtParams.put("sub_id", this.subId);
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

	public Class<SubuserInfoUpdateResponse> getResponseClass() {
		return SubuserInfoUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(subId,"subId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
