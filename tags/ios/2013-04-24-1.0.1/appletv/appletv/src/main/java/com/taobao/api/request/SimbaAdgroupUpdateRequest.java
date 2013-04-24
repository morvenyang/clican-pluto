package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaAdgroupUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.adgroup.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaAdgroupUpdateRequest implements TaobaoRequest<SimbaAdgroupUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 推广组Id
	 */
	private Long adgroupId;

	/** 
	* 默认出价，单位是分，不能小于5
	 */
	private Long defaultPrice;

	/** 
	* 主人昵称
	 */
	private String nick;

	/** 
	* 非搜索出价，单位是分，不能小于5，如果use_nonseatch_default_price为使用默认出价，则此nonsearch_max_price字段传入的数据不起作用，商品将使用默认非搜索出价
	 */
	private Long nonsearchMaxPrice;

	/** 
	* 用户设置的上下线状态 offline-下线(暂停竞价)； online-上线；默认为online
	 */
	private String onlineStatus;

	/** 
	* 非搜索是否使用默认出价，false-不用；true-使用；默认为true;
	 */
	private Boolean useNonsearchDefaultPrice;

	public void setAdgroupId(Long adgroupId) {
		this.adgroupId = adgroupId;
	}
	public Long getAdgroupId() {
		return this.adgroupId;
	}

	public void setDefaultPrice(Long defaultPrice) {
		this.defaultPrice = defaultPrice;
	}
	public Long getDefaultPrice() {
		return this.defaultPrice;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}

	public void setNonsearchMaxPrice(Long nonsearchMaxPrice) {
		this.nonsearchMaxPrice = nonsearchMaxPrice;
	}
	public Long getNonsearchMaxPrice() {
		return this.nonsearchMaxPrice;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	public String getOnlineStatus() {
		return this.onlineStatus;
	}

	public void setUseNonsearchDefaultPrice(Boolean useNonsearchDefaultPrice) {
		this.useNonsearchDefaultPrice = useNonsearchDefaultPrice;
	}
	public Boolean getUseNonsearchDefaultPrice() {
		return this.useNonsearchDefaultPrice;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.adgroup.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("adgroup_id", this.adgroupId);
		txtParams.put("default_price", this.defaultPrice);
		txtParams.put("nick", this.nick);
		txtParams.put("nonsearch_max_price", this.nonsearchMaxPrice);
		txtParams.put("online_status", this.onlineStatus);
		txtParams.put("use_nonsearch_default_price", this.useNonsearchDefaultPrice);
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

	public Class<SimbaAdgroupUpdateResponse> getResponseClass() {
		return SimbaAdgroupUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(adgroupId,"adgroupId");
		RequestCheckUtils.checkMinValue(defaultPrice,5L,"defaultPrice");
		RequestCheckUtils.checkMinValue(nonsearchMaxPrice,5L,"nonsearchMaxPrice");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
