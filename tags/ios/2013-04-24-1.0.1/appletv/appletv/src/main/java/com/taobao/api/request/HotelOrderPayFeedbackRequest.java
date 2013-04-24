package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.HotelOrderPayFeedbackResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.hotel.order.pay.feedback request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class HotelOrderPayFeedbackRequest implements TaobaoRequest<HotelOrderPayFeedbackResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 失败原因,当result为failed时,此项为必填，最长200个字符
	 */
	private String failedReason;

	/** 
	* 指令消息中的messageid,最长128字符
	 */
	private String messageId;

	/** 
	* 酒店订单id
	 */
	private Long oid;

	/** 
	* 合作方订单号,最长250个字符
	 */
	private String outOid;

	/** 
	* 预订结果 
S:成功
F:失败
	 */
	private String result;

	/** 
	* 指令消息中的session_id
	 */
	private Long sessionId;

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	public String getFailedReason() {
		return this.failedReason;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageId() {
		return this.messageId;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}
	public Long getOid() {
		return this.oid;
	}

	public void setOutOid(String outOid) {
		this.outOid = outOid;
	}
	public String getOutOid() {
		return this.outOid;
	}

	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return this.result;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Long getSessionId() {
		return this.sessionId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.hotel.order.pay.feedback";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("failed_reason", this.failedReason);
		txtParams.put("message_id", this.messageId);
		txtParams.put("oid", this.oid);
		txtParams.put("out_oid", this.outOid);
		txtParams.put("result", this.result);
		txtParams.put("session_id", this.sessionId);
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

	public Class<HotelOrderPayFeedbackResponse> getResponseClass() {
		return HotelOrderPayFeedbackResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(messageId,"messageId");
		RequestCheckUtils.checkMinValue(oid,0L,"oid");
		RequestCheckUtils.checkNotEmpty(outOid,"outOid");
		RequestCheckUtils.checkNotEmpty(result,"result");
		RequestCheckUtils.checkNotEmpty(sessionId,"sessionId");
		RequestCheckUtils.checkMinValue(sessionId,0L,"sessionId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
