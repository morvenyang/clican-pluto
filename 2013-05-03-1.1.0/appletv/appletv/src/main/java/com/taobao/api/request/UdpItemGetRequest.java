package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.UdpItemGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.udp.item.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class UdpItemGetRequest implements TaobaoRequest<UdpItemGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 地区ID
	 */
	private Long area;

	/** 
	* 开始时间
	 */
	private Date beginTime;

	/** 
	* 结束时间
	 */
	private Date endTime;

	/** 
	* 指标ID(参阅指标编号)
	 */
	private String fields;

	/** 
	* 商品ID
	 */
	private Long itemid;

	/** 
	* 备用
	 */
	private String parameters;

	/** 
	* 来源ID
	 */
	private Long source;

	public void setArea(Long area) {
		this.area = area;
	}
	public Long getArea() {
		return this.area;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getEndTime() {
		return this.endTime;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getFields() {
		return this.fields;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	public Long getItemid() {
		return this.itemid;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getParameters() {
		return this.parameters;
	}

	public void setSource(Long source) {
		this.source = source;
	}
	public Long getSource() {
		return this.source;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.udp.item.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("area", this.area);
		txtParams.put("begin_time", this.beginTime);
		txtParams.put("end_time", this.endTime);
		txtParams.put("fields", this.fields);
		txtParams.put("itemid", this.itemid);
		txtParams.put("parameters", this.parameters);
		txtParams.put("source", this.source);
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

	public Class<UdpItemGetResponse> getResponseClass() {
		return UdpItemGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(beginTime,"beginTime");
		RequestCheckUtils.checkNotEmpty(endTime,"endTime");
		RequestCheckUtils.checkNotEmpty(fields,"fields");
		RequestCheckUtils.checkMaxListSize(fields,10,"fields");
		RequestCheckUtils.checkNotEmpty(itemid,"itemid");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
