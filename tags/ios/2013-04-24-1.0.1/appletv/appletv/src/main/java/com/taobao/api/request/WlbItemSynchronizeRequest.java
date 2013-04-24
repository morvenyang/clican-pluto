package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.WlbItemSynchronizeResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.wlb.item.synchronize request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class WlbItemSynchronizeRequest implements TaobaoRequest<WlbItemSynchronizeResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 外部实体ID
	 */
	private Long extEntityId;

	/** 
	* 外部实体类型.存如下值
IC_ITEM   --表示IC商品
IC_SKU    --表示IC最小单位商品
若输入其他值，则按IC_ITEM处理
	 */
	private String extEntityType;

	/** 
	* 商品ID
	 */
	private Long itemId;

	/** 
	* 商品所有人淘宝nick
	 */
	private String userNick;

	public void setExtEntityId(Long extEntityId) {
		this.extEntityId = extEntityId;
	}
	public Long getExtEntityId() {
		return this.extEntityId;
	}

	public void setExtEntityType(String extEntityType) {
		this.extEntityType = extEntityType;
	}
	public String getExtEntityType() {
		return this.extEntityType;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getItemId() {
		return this.itemId;
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
		return "taobao.wlb.item.synchronize";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("ext_entity_id", this.extEntityId);
		txtParams.put("ext_entity_type", this.extEntityType);
		txtParams.put("item_id", this.itemId);
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

	public Class<WlbItemSynchronizeResponse> getResponseClass() {
		return WlbItemSynchronizeResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(extEntityId,"extEntityId");
		RequestCheckUtils.checkNotEmpty(extEntityType,"extEntityType");
		RequestCheckUtils.checkNotEmpty(itemId,"itemId");
		RequestCheckUtils.checkNotEmpty(userNick,"userNick");
		RequestCheckUtils.checkMaxLength(userNick,64,"userNick");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
