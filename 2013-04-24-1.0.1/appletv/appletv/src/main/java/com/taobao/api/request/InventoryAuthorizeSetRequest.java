package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.InventoryAuthorizeSetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.inventory.authorize.set request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class InventoryAuthorizeSetRequest implements TaobaoRequest<InventoryAuthorizeSetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 配额授权方式 
PUBLIC: 全共享
PRIVATE:独享
	 */
	private String authorizeType;

	/** 
	* 授权明细
[{“index”:0,“scItemId”:232323,”scItemCode”:”A232”,”storeCode”:”Kj11”,”inventoryType”:1,”channelFlag”:0,”quotaQuantity”:1000,”nickNameList”:”s108,TY000”，“nickName":"ca11"}]
每次请求的列表数据量不超过50条，如果authorize_type是PUBLIC,使用nickNameList，否则请用nickName
	 */
	private String items;

	public void setAuthorizeType(String authorizeType) {
		this.authorizeType = authorizeType;
	}
	public String getAuthorizeType() {
		return this.authorizeType;
	}

	public void setItems(String items) {
		this.items = items;
	}
	public String getItems() {
		return this.items;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.inventory.authorize.set";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("authorize_type", this.authorizeType);
		txtParams.put("items", this.items);
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

	public Class<InventoryAuthorizeSetResponse> getResponseClass() {
		return InventoryAuthorizeSetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(authorizeType,"authorizeType");
		RequestCheckUtils.checkNotEmpty(items,"items");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
