package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.JipiaoPolicyGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.jipiao.policy.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class JipiaoPolicyGetRequest implements TaobaoRequest<JipiaoPolicyGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* type外0，表示机票政策id；type为1，表示机票政策out_product_id
	 */
	private String policyId;

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
		return "taobao.jipiao.policy.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("policy_id", this.policyId);
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

	public Class<JipiaoPolicyGetResponse> getResponseClass() {
		return JipiaoPolicyGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(policyId,"policyId");
		RequestCheckUtils.checkMaxLength(policyId,64,"policyId");
		RequestCheckUtils.checkNotEmpty(type,"type");
		RequestCheckUtils.checkMaxValue(type,1L,"type");
		RequestCheckUtils.checkMinValue(type,0L,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
