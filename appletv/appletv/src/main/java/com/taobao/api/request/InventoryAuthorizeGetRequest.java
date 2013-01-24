package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.InventoryAuthorizeGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.inventory.authorize.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class InventoryAuthorizeGetRequest implements TaobaoRequest<InventoryAuthorizeGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 库存分配授权结果码
	 */
	private String authorizeCode;

	/** 
	* 分配用户列表，多个用户使用“,“分割开
	 */
	private String userNickList;

	public void setAuthorizeCode(String authorizeCode) {
		this.authorizeCode = authorizeCode;
	}
	public String getAuthorizeCode() {
		return this.authorizeCode;
	}

	public void setUserNickList(String userNickList) {
		this.userNickList = userNickList;
	}
	public String getUserNickList() {
		return this.userNickList;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.inventory.authorize.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("authorize_code", this.authorizeCode);
		txtParams.put("user_nick_list", this.userNickList);
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

	public Class<InventoryAuthorizeGetResponse> getResponseClass() {
		return InventoryAuthorizeGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(authorizeCode,"authorizeCode");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
