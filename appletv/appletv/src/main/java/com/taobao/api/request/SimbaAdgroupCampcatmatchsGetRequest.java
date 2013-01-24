package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaAdgroupCampcatmatchsGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.adgroup.campcatmatchs.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaAdgroupCampcatmatchsGetRequest implements TaobaoRequest<SimbaAdgroupCampcatmatchsGetResponse> {

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
	* 页码，从1开始
	 */
	private Long pageNo;

	/** 
	* 页尺寸，最大200
	 */
	private Long pageSize;

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

	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}
	public Long getPageNo() {
		return this.pageNo;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageSize() {
		return this.pageSize;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.adgroup.campcatmatchs.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("campaign_id", this.campaignId);
		txtParams.put("nick", this.nick);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
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

	public Class<SimbaAdgroupCampcatmatchsGetResponse> getResponseClass() {
		return SimbaAdgroupCampcatmatchsGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(campaignId,"campaignId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
