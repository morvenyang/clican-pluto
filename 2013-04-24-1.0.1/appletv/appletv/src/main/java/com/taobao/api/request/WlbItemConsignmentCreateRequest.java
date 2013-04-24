package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.WlbItemConsignmentCreateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.wlb.item.consignment.create request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class WlbItemConsignmentCreateRequest implements TaobaoRequest<WlbItemConsignmentCreateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 商品id
	 */
	private Long itemId;

	/** 
	* 代销数量
	 */
	private Long number;

	/** 
	* 货主商品id
	 */
	private Long ownerItemId;

	/** 
	* 货主id
	 */
	private Long ownerUserId;

	/** 
	* 通过taobao.wlb.item.authorization.add接口创建后得到的rule_id，规则中设定了代销商可以代销的商品数量
	 */
	private Long ruleId;

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getItemId() {
		return this.itemId;
	}

	public void setNumber(Long number) {
		this.number = number;
	}
	public Long getNumber() {
		return this.number;
	}

	public void setOwnerItemId(Long ownerItemId) {
		this.ownerItemId = ownerItemId;
	}
	public Long getOwnerItemId() {
		return this.ownerItemId;
	}

	public void setOwnerUserId(Long ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	public Long getOwnerUserId() {
		return this.ownerUserId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public Long getRuleId() {
		return this.ruleId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.wlb.item.consignment.create";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("item_id", this.itemId);
		txtParams.put("number", this.number);
		txtParams.put("owner_item_id", this.ownerItemId);
		txtParams.put("owner_user_id", this.ownerUserId);
		txtParams.put("rule_id", this.ruleId);
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

	public Class<WlbItemConsignmentCreateResponse> getResponseClass() {
		return WlbItemConsignmentCreateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(itemId,"itemId");
		RequestCheckUtils.checkNotEmpty(number,"number");
		RequestCheckUtils.checkNotEmpty(ownerItemId,"ownerItemId");
		RequestCheckUtils.checkNotEmpty(ownerUserId,"ownerUserId");
		RequestCheckUtils.checkNotEmpty(ruleId,"ruleId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
