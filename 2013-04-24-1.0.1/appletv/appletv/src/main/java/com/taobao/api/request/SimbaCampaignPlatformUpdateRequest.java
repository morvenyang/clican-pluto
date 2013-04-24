package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaCampaignPlatformUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.campaign.platform.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaCampaignPlatformUpdateRequest implements TaobaoRequest<SimbaCampaignPlatformUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 推广计划Id
	 */
	private Long campaignId;

	/** 
	* 主人昵称
	 */
	private String nick;

	/** 
	* 非搜索投放频道代码数组，频道代码必须是直通车非搜索类频道列表中的值。
	 */
	private String nonsearchChannels;

	/** 
	* 溢价的百分比，必须是大于等于 1小于等于200的整数
	 */
	private Long outsideDiscount;

	/** 
	* 搜索投放频道代码数组，频道代码必须是直通车搜索类频道列表中的值，必须包含淘宝内网。
	 */
	private String searchChannels;

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

	public void setNonsearchChannels(String nonsearchChannels) {
		this.nonsearchChannels = nonsearchChannels;
	}
	public String getNonsearchChannels() {
		return this.nonsearchChannels;
	}

	public void setOutsideDiscount(Long outsideDiscount) {
		this.outsideDiscount = outsideDiscount;
	}
	public Long getOutsideDiscount() {
		return this.outsideDiscount;
	}

	public void setSearchChannels(String searchChannels) {
		this.searchChannels = searchChannels;
	}
	public String getSearchChannels() {
		return this.searchChannels;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.campaign.platform.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("campaign_id", this.campaignId);
		txtParams.put("nick", this.nick);
		txtParams.put("nonsearch_channels", this.nonsearchChannels);
		txtParams.put("outside_discount", this.outsideDiscount);
		txtParams.put("search_channels", this.searchChannels);
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

	public Class<SimbaCampaignPlatformUpdateResponse> getResponseClass() {
		return SimbaCampaignPlatformUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(campaignId,"campaignId");
		RequestCheckUtils.checkMaxListSize(nonsearchChannels,10,"nonsearchChannels");
		RequestCheckUtils.checkNotEmpty(outsideDiscount,"outsideDiscount");
		RequestCheckUtils.checkMaxValue(outsideDiscount,200L,"outsideDiscount");
		RequestCheckUtils.checkMinValue(outsideDiscount,1L,"outsideDiscount");
		RequestCheckUtils.checkNotEmpty(searchChannels,"searchChannels");
		RequestCheckUtils.checkMaxListSize(searchChannels,10,"searchChannels");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
