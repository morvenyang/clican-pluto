package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FenxiaoGradeDeleteResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.fenxiao.grade.delete request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class FenxiaoGradeDeleteRequest implements TaobaoRequest<FenxiaoGradeDeleteResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 等级ID
	 */
	private Long gradeId;

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}
	public Long getGradeId() {
		return this.gradeId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.fenxiao.grade.delete";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("grade_id", this.gradeId);
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

	public Class<FenxiaoGradeDeleteResponse> getResponseClass() {
		return FenxiaoGradeDeleteResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(gradeId,"gradeId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
