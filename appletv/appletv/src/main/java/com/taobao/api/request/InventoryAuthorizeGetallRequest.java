package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.InventoryAuthorizeGetallResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.inventory.authorize.getall request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class InventoryAuthorizeGetallRequest implements TaobaoRequest<InventoryAuthorizeGetallResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 商品编码列表，使用”,”分割多个号码，最大50个
	 */
	private String scItemIdList;

	/** 
	* 指定的商家仓库编码，使用”,”分割多个仓库
	 */
	private String storeCodeList;

	public void setScItemIdList(String scItemIdList) {
		this.scItemIdList = scItemIdList;
	}
	public String getScItemIdList() {
		return this.scItemIdList;
	}

	public void setStoreCodeList(String storeCodeList) {
		this.storeCodeList = storeCodeList;
	}
	public String getStoreCodeList() {
		return this.storeCodeList;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.inventory.authorize.getall";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("sc_item_id_list", this.scItemIdList);
		txtParams.put("store_code_list", this.storeCodeList);
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

	public Class<InventoryAuthorizeGetallResponse> getResponseClass() {
		return InventoryAuthorizeGetallResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(scItemIdList,"scItemIdList");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
