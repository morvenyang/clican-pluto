package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.IncrementCustomerStopResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.increment.customer.stop request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class IncrementCustomerStopRequest implements TaobaoRequest<IncrementCustomerStopResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 应用要关闭增量消息服务的用户昵称
	 */
	private String nick;

	/** 
	* 应用需要关闭用户的功能。取值可为get,notify和syn分别表示增量api取消息，主动发送消息和同步数据功能。用户关闭相应功能前,需应用已为用户经开通了相应的功能。这三个参数可无序任意组合。在关闭时，type里面的参数会根据应用订阅的类型进行相应的过虑。如应用只订阅主动通知，则默认值过滤后为get,notify，如果应用只订阅数据同步服务，默认值过滤后为syn。
	 */
	private String type;

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
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
		return "taobao.increment.customer.stop";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("nick", this.nick);
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

	public Class<IncrementCustomerStopResponse> getResponseClass() {
		return IncrementCustomerStopResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(nick,"nick");
		RequestCheckUtils.checkMaxListSize(type,3,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
