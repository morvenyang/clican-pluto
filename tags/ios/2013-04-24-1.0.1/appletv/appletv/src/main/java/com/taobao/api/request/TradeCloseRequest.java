package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TradeCloseResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.trade.close request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TradeCloseRequest implements TaobaoRequest<TradeCloseResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 交易关闭原因。
可以选择的理由有：
1、买家不想买了
2、信息填写错误，重新拍
3、卖家缺货
4、同城见面交易
5、其他原因
注：尽量不要传入自定义的关闭理由
	 */
	private String closeReason;

	/** 
	* 主订单或子订单编号。
	 */
	private Long tid;

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}
	public String getCloseReason() {
		return this.closeReason;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}
	public Long getTid() {
		return this.tid;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.trade.close";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("close_reason", this.closeReason);
		txtParams.put("tid", this.tid);
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

	public Class<TradeCloseResponse> getResponseClass() {
		return TradeCloseResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(closeReason,"closeReason");
		RequestCheckUtils.checkNotEmpty(tid,"tid");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
