package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SellercenterRolesGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.sellercenter.roles.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SellercenterRolesGetRequest implements TaobaoRequest<SellercenterRolesGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 卖家昵称(只允许查询自己的信息：当前登陆者)
	 */
	private String nick;

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.sellercenter.roles.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("nick", this.nick);
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

	public Class<SellercenterRolesGetResponse> getResponseClass() {
		return SellercenterRolesGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(nick,"nick");
		RequestCheckUtils.checkMaxLength(nick,500,"nick");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
