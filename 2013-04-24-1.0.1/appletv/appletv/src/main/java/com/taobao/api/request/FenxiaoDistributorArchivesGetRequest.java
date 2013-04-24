package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FenxiaoDistributorArchivesGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.fenxiao.distributor.archives.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class FenxiaoDistributorArchivesGetRequest implements TaobaoRequest<FenxiaoDistributorArchivesGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 分销商淘宝店主nick
	 */
	private String distributorUserNick;

	public void setDistributorUserNick(String distributorUserNick) {
		this.distributorUserNick = distributorUserNick;
	}
	public String getDistributorUserNick() {
		return this.distributorUserNick;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.fenxiao.distributor.archives.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("distributor_user_nick", this.distributorUserNick);
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

	public Class<FenxiaoDistributorArchivesGetResponse> getResponseClass() {
		return FenxiaoDistributorArchivesGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(distributorUserNick,"distributorUserNick");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
