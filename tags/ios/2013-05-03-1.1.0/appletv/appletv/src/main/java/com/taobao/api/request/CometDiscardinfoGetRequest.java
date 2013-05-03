package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.CometDiscardinfoGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.comet.discardinfo.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class CometDiscardinfoGetRequest implements TaobaoRequest<CometDiscardinfoGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 指定截止日志，如果不传则为服务端当前时间
	 */
	private Date end;

	/** 
	* 用户nick
	 */
	private String nick;

	/** 
	* 指定从那个时间开始找丢弃的消息
	 */
	private Date start;

	/** 
	* 指定多个消息类型
	 */
	private String types;

	/** 
	* 指定查看那个用户的丢弃消息
	 */
	private Long userId;

	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getEnd() {
		return this.end;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	public Date getStart() {
		return this.start;
	}

	public void setTypes(String types) {
		this.types = types;
	}
	public String getTypes() {
		return this.types;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserId() {
		return this.userId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.comet.discardinfo.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("end", this.end);
		txtParams.put("nick", this.nick);
		txtParams.put("start", this.start);
		txtParams.put("types", this.types);
		txtParams.put("user_id", this.userId);
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

	public Class<CometDiscardinfoGetResponse> getResponseClass() {
		return CometDiscardinfoGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(start,"start");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
