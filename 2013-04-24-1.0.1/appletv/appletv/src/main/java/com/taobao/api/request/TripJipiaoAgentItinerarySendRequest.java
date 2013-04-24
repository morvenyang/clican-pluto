package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TripJipiaoAgentItinerarySendResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.trip.jipiao.agent.itinerary.send request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TripJipiaoAgentItinerarySendRequest implements TaobaoRequest<TripJipiaoAgentItinerarySendResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 物流公司代码CODE，如不清楚，请找运营提供
	 */
	private String companyCode;

	/** 
	* 邮寄单号，长度不能超过32字节
	 */
	private String expressCode;

	/** 
	* 淘宝系统行程单唯一键
	 */
	private Long itineraryId;

	/** 
	* 行程单号
	 */
	private String itineraryNo;

	/** 
	* 邮寄日期，格式yyyy-mm-dd
	 */
	private String sendDate;

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getExpressCode() {
		return this.expressCode;
	}

	public void setItineraryId(Long itineraryId) {
		this.itineraryId = itineraryId;
	}
	public Long getItineraryId() {
		return this.itineraryId;
	}

	public void setItineraryNo(String itineraryNo) {
		this.itineraryNo = itineraryNo;
	}
	public String getItineraryNo() {
		return this.itineraryNo;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getSendDate() {
		return this.sendDate;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.trip.jipiao.agent.itinerary.send";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("company_code", this.companyCode);
		txtParams.put("express_code", this.expressCode);
		txtParams.put("itinerary_id", this.itineraryId);
		txtParams.put("itinerary_no", this.itineraryNo);
		txtParams.put("send_date", this.sendDate);
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

	public Class<TripJipiaoAgentItinerarySendResponse> getResponseClass() {
		return TripJipiaoAgentItinerarySendResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(companyCode,"companyCode");
		RequestCheckUtils.checkMaxLength(companyCode,20,"companyCode");
		RequestCheckUtils.checkNotEmpty(expressCode,"expressCode");
		RequestCheckUtils.checkMaxLength(expressCode,32,"expressCode");
		RequestCheckUtils.checkNotEmpty(itineraryId,"itineraryId");
		RequestCheckUtils.checkNotEmpty(itineraryNo,"itineraryNo");
		RequestCheckUtils.checkMaxLength(itineraryNo,32,"itineraryNo");
		RequestCheckUtils.checkNotEmpty(sendDate,"sendDate");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
