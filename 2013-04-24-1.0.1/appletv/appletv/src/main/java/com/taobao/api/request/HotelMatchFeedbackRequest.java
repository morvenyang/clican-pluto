package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.HotelMatchFeedbackResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.hotel.match.feedback request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class HotelMatchFeedbackRequest implements TaobaoRequest<HotelMatchFeedbackResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 需进行匹配的酒店id
	 */
	private Long haid;

	/** 
	* 匹配命中的酒店id
	 */
	private Long hid;

	/** 
	* 匹配结果 0:未匹配，1:匹配
	 */
	private Long matchResult;

	public void setHaid(Long haid) {
		this.haid = haid;
	}
	public Long getHaid() {
		return this.haid;
	}

	public void setHid(Long hid) {
		this.hid = hid;
	}
	public Long getHid() {
		return this.hid;
	}

	public void setMatchResult(Long matchResult) {
		this.matchResult = matchResult;
	}
	public Long getMatchResult() {
		return this.matchResult;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.hotel.match.feedback";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("haid", this.haid);
		txtParams.put("hid", this.hid);
		txtParams.put("match_result", this.matchResult);
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

	public Class<HotelMatchFeedbackResponse> getResponseClass() {
		return HotelMatchFeedbackResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(haid,"haid");
		RequestCheckUtils.checkMinValue(haid,1L,"haid");
		RequestCheckUtils.checkMinValue(hid,0L,"hid");
		RequestCheckUtils.checkNotEmpty(matchResult,"matchResult");
		RequestCheckUtils.checkMaxValue(matchResult,1L,"matchResult");
		RequestCheckUtils.checkMinValue(matchResult,0L,"matchResult");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
