package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SubuserDutyAddResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.subuser.duty.add request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SubuserDutyAddRequest implements TaobaoRequest<SubuserDutyAddResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 职务级别
	 */
	private Long dutyLevel;

	/** 
	* 职务名称
	 */
	private String dutyName;

	/** 
	* 主账号用户名
	 */
	private String userNick;

	public void setDutyLevel(Long dutyLevel) {
		this.dutyLevel = dutyLevel;
	}
	public Long getDutyLevel() {
		return this.dutyLevel;
	}

	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getDutyName() {
		return this.dutyName;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	public String getUserNick() {
		return this.userNick;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.subuser.duty.add";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("duty_level", this.dutyLevel);
		txtParams.put("duty_name", this.dutyName);
		txtParams.put("user_nick", this.userNick);
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

	public Class<SubuserDutyAddResponse> getResponseClass() {
		return SubuserDutyAddResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(dutyLevel,"dutyLevel");
		RequestCheckUtils.checkNotEmpty(dutyName,"dutyName");
		RequestCheckUtils.checkNotEmpty(userNick,"userNick");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
