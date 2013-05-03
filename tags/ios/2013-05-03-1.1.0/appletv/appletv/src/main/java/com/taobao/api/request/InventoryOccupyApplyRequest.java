package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.InventoryOccupyApplyResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.inventory.occupy.apply request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class InventoryOccupyApplyRequest implements TaobaoRequest<InventoryOccupyApplyResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 外部订单类型, BALANCE:盘点、NON_TAOBAO_TRADE:非淘宝交易、ALLOCATE:调拨、OTHERS:其他
	 */
	private String bizType;

	/** 
	* 商家外部定单号
	 */
	private String bizUniqueCode;

	/** 
	* 渠道编号
	 */
	private String channelFlags;

	/** 
	* 商品库存预留信息： [{"scItemId":"商品后端ID，如果有传scItemCode,参数可以为0","scItemCode":"商品商家编码","inventoryType":"库存类型  1：正常,2：损坏,3：冻结,10：质押",11-20:商家自定义,”inventoryTypeName”:”库存类型名称,可选”,"occupyQuantity":"数量"}]
	 */
	private String items;

	/** 
	* 天数，默认5天，最大15天
	 */
	private Long occupyTime;

	/** 
	* 占用类型
参数定义
AUTO_CALCULATE:自动计算可供占用，如果库存不够返回失败 ClIENT_FORCE：强制要求最大化占用，有多少占用多少
	 */
	private String occupyType;

	/** 
	* 商家仓库编码
	 */
	private String storeCode;

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getBizType() {
		return this.bizType;
	}

	public void setBizUniqueCode(String bizUniqueCode) {
		this.bizUniqueCode = bizUniqueCode;
	}
	public String getBizUniqueCode() {
		return this.bizUniqueCode;
	}

	public void setChannelFlags(String channelFlags) {
		this.channelFlags = channelFlags;
	}
	public String getChannelFlags() {
		return this.channelFlags;
	}

	public void setItems(String items) {
		this.items = items;
	}
	public String getItems() {
		return this.items;
	}

	public void setOccupyTime(Long occupyTime) {
		this.occupyTime = occupyTime;
	}
	public Long getOccupyTime() {
		return this.occupyTime;
	}

	public void setOccupyType(String occupyType) {
		this.occupyType = occupyType;
	}
	public String getOccupyType() {
		return this.occupyType;
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
		return "taobao.inventory.occupy.apply";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("biz_type", this.bizType);
		txtParams.put("biz_unique_code", this.bizUniqueCode);
		txtParams.put("channel_flags", this.channelFlags);
		txtParams.put("items", this.items);
		txtParams.put("occupy_time", this.occupyTime);
		txtParams.put("occupy_type", this.occupyType);
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

	public Class<InventoryOccupyApplyResponse> getResponseClass() {
		return InventoryOccupyApplyResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(bizType,"bizType");
		RequestCheckUtils.checkNotEmpty(bizUniqueCode,"bizUniqueCode");
		RequestCheckUtils.checkNotEmpty(items,"items");
		RequestCheckUtils.checkNotEmpty(occupyTime,"occupyTime");
		RequestCheckUtils.checkNotEmpty(occupyType,"occupyType");
		RequestCheckUtils.checkNotEmpty(storeCode,"storeCode");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
