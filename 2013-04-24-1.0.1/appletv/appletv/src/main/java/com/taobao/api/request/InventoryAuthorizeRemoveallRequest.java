package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.InventoryAuthorizeRemoveallResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.inventory.authorize.removeall request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class InventoryAuthorizeRemoveallRequest implements TaobaoRequest<InventoryAuthorizeRemoveallResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 商品编码列表，用”,”隔开.每次请求的商品列表不超过50个
	 */
	private String scItemIdList;

	/** 
	* 移除授权的目标用户昵称列表，用”,”隔开
	 */
	private String userNickList;

	public void setScItemIdList(String scItemIdList) {
		this.scItemIdList = scItemIdList;
	}
	public String getScItemIdList() {
		return this.scItemIdList;
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
		return "taobao.inventory.authorize.removeall";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("sc_item_id_list", this.scItemIdList);
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

	public Class<InventoryAuthorizeRemoveallResponse> getResponseClass() {
		return InventoryAuthorizeRemoveallResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(scItemIdList,"scItemIdList");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
