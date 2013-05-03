package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.JipiaoPolicystatusUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.jipiao.policystatus.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class JipiaoPolicystatusUpdateRequest implements TaobaoRequest<JipiaoPolicystatusUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* type为0，表示机票政策id；type为1，表示机票政策out_product_id；最大支持政策数100，注意不要如果不要超出字符串的长度限制，超出的话，请调小批量的个数
	 */
	private String policyId;

	/** 
	* 政策的状态: 0，挂起；1，解挂；2，删除
	 */
	private Long status;

	/** 
	* 0，表示按政策id进行查询；1，表示按政策外部id进行查询
	 */
	private Long type;

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	public String getPolicyId() {
		return this.policyId;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getStatus() {
		return this.status;
	}

	public void setType(Long type) {
		this.type = type;
	}
	public Long getType() {
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
		return "taobao.jipiao.policystatus.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("policy_id", this.policyId);
		txtParams.put("status", this.status);
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

	public Class<JipiaoPolicystatusUpdateResponse> getResponseClass() {
		return JipiaoPolicystatusUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(policyId,"policyId");
		RequestCheckUtils.checkMaxListSize(policyId,100,"policyId");
		RequestCheckUtils.checkMaxLength(policyId,6500,"policyId");
		RequestCheckUtils.checkNotEmpty(status,"status");
		RequestCheckUtils.checkNotEmpty(type,"type");
		RequestCheckUtils.checkMaxValue(type,1L,"type");
		RequestCheckUtils.checkMinValue(type,0L,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
