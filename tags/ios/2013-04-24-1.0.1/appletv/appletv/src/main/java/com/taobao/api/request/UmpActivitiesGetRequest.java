package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UmpActivitiesGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.ump.activities.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UmpActivitiesGetRequest implements TaobaoRequest<UmpActivitiesGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 分页的页码
	 */
	private Long pageNo;

	/** 
	* 每页的最大条数
	 */
	private Long pageSize;

	/** 
	* 工具id
	 */
	private Long toolId;

	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}
	public Long getPageNo() {
		return this.pageNo;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageSize() {
		return this.pageSize;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}
	public Long getToolId() {
		return this.toolId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.ump.activities.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("tool_id", this.toolId);
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

	public Class<UmpActivitiesGetResponse> getResponseClass() {
		return UmpActivitiesGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(pageNo,"pageNo");
		RequestCheckUtils.checkMinValue(pageNo,0L,"pageNo");
		RequestCheckUtils.checkNotEmpty(pageSize,"pageSize");
		RequestCheckUtils.checkMaxValue(pageSize,50L,"pageSize");
		RequestCheckUtils.checkMinValue(pageSize,1L,"pageSize");
		RequestCheckUtils.checkNotEmpty(toolId,"toolId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
