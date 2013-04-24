package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.RdsDbDeleteResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.rds.db.delete request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class RdsDbDeleteRequest implements TaobaoRequest<RdsDbDeleteResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 数据库的ID，可以通过 taobao.rds.db.get 获取
	 */
	private Long dbId;

	/** 
	* rds的实例名
	 */
	private String instanceName;

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}
	public Long getDbId() {
		return this.dbId;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getInstanceName() {
		return this.instanceName;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.rds.db.delete";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("db_id", this.dbId);
		txtParams.put("instance_name", this.instanceName);
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

	public Class<RdsDbDeleteResponse> getResponseClass() {
		return RdsDbDeleteResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(dbId,"dbId");
		RequestCheckUtils.checkNotEmpty(instanceName,"instanceName");
		RequestCheckUtils.checkMaxLength(instanceName,30,"instanceName");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
