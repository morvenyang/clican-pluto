package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FeedbackAddResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.feedback.add request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class FeedbackAddRequest implements TaobaoRequest<FeedbackAddResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 具体反馈的信息，一个json构成的字符串
	 */
	private String info;

	/** 
	* 反馈信息的类型，例如是同步服务的或者其他系统的
	 */
	private String type;

	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfo() {
		return this.info;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.feedback.add";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("info", this.info);
		txtParams.put("type", this.type);
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

	public Class<FeedbackAddResponse> getResponseClass() {
		return FeedbackAddResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(info,"info");
		RequestCheckUtils.checkNotEmpty(type,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
