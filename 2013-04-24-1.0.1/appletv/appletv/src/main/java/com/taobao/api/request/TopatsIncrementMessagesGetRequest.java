package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TopatsIncrementMessagesGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.topats.increment.messages.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TopatsIncrementMessagesGetRequest implements TaobaoRequest<TopatsIncrementMessagesGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 消息结束时间，格式：yyyy-MM-dd HH:mm:ss，其中start < end < 现在，并且start和end在同一天。
	 */
	private Date end;

	/** 
	* 消息开始时间，格式：yyyy-MM-dd HH:mm:ss，其中start >= 前天零点
	 */
	private Date start;

	/** 
	* 消息类型，多个类型之间用半角逗号分隔，可选值为：item,trade,refund。
	 */
	private String topics;

	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getEnd() {
		return this.end;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	public Date getStart() {
		return this.start;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}
	public String getTopics() {
		return this.topics;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.topats.increment.messages.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("end", this.end);
		txtParams.put("start", this.start);
		txtParams.put("topics", this.topics);
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

	public Class<TopatsIncrementMessagesGetResponse> getResponseClass() {
		return TopatsIncrementMessagesGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(end,"end");
		RequestCheckUtils.checkNotEmpty(start,"start");
		RequestCheckUtils.checkNotEmpty(topics,"topics");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
