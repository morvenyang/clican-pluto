package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.WlbReplenishStatisticsResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.wlb.replenish.statistics request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class WlbReplenishStatisticsRequest implements TaobaoRequest<WlbReplenishStatisticsResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 商品编码
	 */
	private String itemCode;

	/** 
	* 商品名称
	 */
	private String name;

	/** 
	* 分页参数，默认第一页
	 */
	private Long pageNo;

	/** 
	* 分页每页页数，默认20，最大50
	 */
	private Long pageSize;

	/** 
	* 仓库编码
	 */
	private String storeCode;

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemCode() {
		return this.itemCode;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
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

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getStoreCode() {
		return this.storeCode;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.wlb.replenish.statistics";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("item_code", this.itemCode);
		txtParams.put("name", this.name);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("store_code", this.storeCode);
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

	public Class<WlbReplenishStatisticsResponse> getResponseClass() {
		return WlbReplenishStatisticsResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkMaxValue(pageSize,50L,"pageSize");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
