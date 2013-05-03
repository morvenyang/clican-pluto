package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.IncrementCustomersGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.increment.customers.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class IncrementCustomersGetRequest implements TaobaoRequest<IncrementCustomersGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 需要返回的字段。可填写的字段参见AppCustomer中的返回字段。如：nick,created,status,type,subscriptions。
	 */
	private String fields;

	/** 
	* 查询用户的昵称。当为空时通过分页方式查询appkey开通的所有用户,最多填入20个昵称。
	 */
	private String nicks;

	/** 
	* 分页查询时，查询的页码。此参数只有nicks为空时起作用。
	 */
	private Long pageNo;

	/** 
	* 分布查询时，页的大小。此参数只有当nicks为空时起作用。
	 */
	private Long pageSize;

	/** 
	* 查询用户开通的功能。值可为get,notify和syn分别表示增量api取消息，主动发送消息和同步数据功能。这三个值不分次序。在查询时，type里面的参数会根据应用订阅的类型进行相应的过虑。如应用只订阅主动通知，则默认值过滤后为get,notify，如果应用只订阅数据同步服务，默认值过滤后为syn。
	 */
	private String type;

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setNicks(String nicks) {
		this.nicks = nicks;
	}
	public String getNicks() {
		return this.nicks;
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

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
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
		return "taobao.increment.customers.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("fields", this.fields);
		txtParams.put("nicks", this.nicks);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
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

	public Class<IncrementCustomersGetResponse> getResponseClass() {
		return IncrementCustomersGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkMaxListSize(nicks,20,"nicks");
		RequestCheckUtils.checkMinValue(pageNo,0L,"pageNo");
		RequestCheckUtils.checkMaxValue(pageSize,200L,"pageSize");
		RequestCheckUtils.checkMinValue(pageSize,0L,"pageSize");
		RequestCheckUtils.checkMaxListSize(type,3,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
