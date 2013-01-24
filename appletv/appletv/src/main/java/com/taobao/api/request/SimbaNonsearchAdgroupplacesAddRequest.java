package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaNonsearchAdgroupplacesAddResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.nonsearch.adgroupplaces.add request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaNonsearchAdgroupplacesAddRequest implements TaobaoRequest<SimbaNonsearchAdgroupplacesAddResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 宝贝id投放位置id json数组字符串，数组个数最多200个。其中json数组中的key必须和对应实体AdGroupPlace中的属性字段保持一致，否则对应的实体对象属性获取不到相应的值
	 */
	private String adgroupPlacesJson;

	/** 
	* 12345
	 */
	private Long campaignId;

	/** 
	* 主人昵称
	 */
	private String nick;

	public void setAdgroupPlacesJson(String adgroupPlacesJson) {
		this.adgroupPlacesJson = adgroupPlacesJson;
	}
	public String getAdgroupPlacesJson() {
		return this.adgroupPlacesJson;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}
	public Long getCampaignId() {
		return this.campaignId;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.nonsearch.adgroupplaces.add";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("adgroup_places_json", this.adgroupPlacesJson);
		txtParams.put("campaign_id", this.campaignId);
		txtParams.put("nick", this.nick);
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

	public Class<SimbaNonsearchAdgroupplacesAddResponse> getResponseClass() {
		return SimbaNonsearchAdgroupplacesAddResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(adgroupPlacesJson,"adgroupPlacesJson");
		RequestCheckUtils.checkNotEmpty(campaignId,"campaignId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
