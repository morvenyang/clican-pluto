package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.InventoryOccupyCancelResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.inventory.occupy.cancel request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class InventoryOccupyCancelRequest implements TaobaoRequest<InventoryOccupyCancelResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 商家外部定单号
	 */
	private String bizUniqueCode;

	/** 
	* 申请预留时的操作返回码
	 */
	private String operateCode;

	/** 
	* 商家仓库编码
	 */
	private String storeCode;

	public void setBizUniqueCode(String bizUniqueCode) {
		this.bizUniqueCode = bizUniqueCode;
	}
	public String getBizUniqueCode() {
		return this.bizUniqueCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}
	public String getOperateCode() {
		return this.operateCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getStoreCode() {
		return this.storeCode;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.inventory.occupy.cancel";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("biz_unique_code", this.bizUniqueCode);
		txtParams.put("operate_code", this.operateCode);
		txtParams.put("store_code", this.storeCode);
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

	public Class<InventoryOccupyCancelResponse> getResponseClass() {
		return InventoryOccupyCancelResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(bizUniqueCode,"bizUniqueCode");
		RequestCheckUtils.checkNotEmpty(operateCode,"operateCode");
		RequestCheckUtils.checkNotEmpty(storeCode,"storeCode");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
