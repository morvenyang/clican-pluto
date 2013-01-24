package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaCatmatchidsDeletedGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.catmatchids.deleted.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaCatmatchidsDeletedGetRequest implements TaobaoRequest<SimbaCatmatchidsDeletedGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 主人昵称
	 */
	private String nick;

	/** 
	* 返回的第几页数据，默认为1
	 */
	private Long pageNo;

	/** 
	* 返回的每页数据量大小,默认200最大1000
	 */
	private Long pageSize;

	/** 
	* 得到此时间点之后的数据，不能大于一个月
	 */
	private Date startTime;

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
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

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStartTime() {
		return this.startTime;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.catmatchids.deleted.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("nick", this.nick);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("start_time", this.startTime);
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

	public Class<SimbaCatmatchidsDeletedGetResponse> getResponseClass() {
		return SimbaCatmatchidsDeletedGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(startTime,"startTime");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
