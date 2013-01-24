package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaCampaignBudgetUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.campaign.budget.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaCampaignBudgetUpdateRequest implements TaobaoRequest<SimbaCampaignBudgetUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 如果为空则取消限额；否则必须为整数，单位是元，不得小于30；
	 */
	private Long budget;

	/** 
	* 推广计划Id
	 */
	private Long campaignId;

	/** 
	* 主人昵称
	 */
	private String nick;

	/** 
	* 是否平滑消耗：false-否，true-是
	 */
	private Boolean useSmooth;

	public void setBudget(Long budget) {
		this.budget = budget;
	}
	public Long getBudget() {
		return this.budget;
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

	public void setUseSmooth(Boolean useSmooth) {
		this.useSmooth = useSmooth;
	}
	public Boolean getUseSmooth() {
		return this.useSmooth;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.campaign.budget.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("budget", this.budget);
		txtParams.put("campaign_id", this.campaignId);
		txtParams.put("nick", this.nick);
		txtParams.put("use_smooth", this.useSmooth);
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

	public Class<SimbaCampaignBudgetUpdateResponse> getResponseClass() {
		return SimbaCampaignBudgetUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkMaxValue(budget,99999L,"budget");
		RequestCheckUtils.checkMinValue(budget,30L,"budget");
		RequestCheckUtils.checkNotEmpty(campaignId,"campaignId");
		RequestCheckUtils.checkNotEmpty(useSmooth,"useSmooth");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
