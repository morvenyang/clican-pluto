package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.WlbOrderitemPageGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.wlb.orderitem.page.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class WlbOrderitemPageGetRequest implements TaobaoRequest<WlbOrderitemPageGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 物流宝订单编码
	 */
	private String orderCode;

	/** 
	* 分页查询参数，指定查询页数，默认为1
	 */
	private Long pageNo;

	/** 
	* 分页查询参数，每页查询数量，默认20，最大值50,大于50时按照50条查询
	 */
	private Long pageSize;

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOrderCode() {
		return this.orderCode;
	}

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
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.wlb.orderitem.page.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("order_code", this.orderCode);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
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

	public Class<WlbOrderitemPageGetResponse> getResponseClass() {
		return WlbOrderitemPageGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(orderCode,"orderCode");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
