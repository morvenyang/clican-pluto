package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TaobaokeWidgetUrlConvertResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.taobaoke.widget.url.convert request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class TaobaokeWidgetUrlConvertRequest implements TaobaoRequest<TaobaokeWidgetUrlConvertResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 自定义输入串.格式:英文和数字组成;长度不能大于12个字符,区分不同的推广渠道,如:bbs,表示bbs为推广渠道;blog,表示blog为推广渠道
	 */
	private String outerCode;

	/** 
	* 需要转化为淘客链接的url，可以把天猫、聚划算、淘宝旅行、淘宝游戏等平台的活动链接转换为淘宝客推广链接
	 */
	private String url;

	public void setOuterCode(String outerCode) {
		this.outerCode = outerCode;
	}
	public String getOuterCode() {
		return this.outerCode;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return this.url;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.taobaoke.widget.url.convert";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("outer_code", this.outerCode);
		txtParams.put("url", this.url);
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

	public Class<TaobaokeWidgetUrlConvertResponse> getResponseClass() {
		return TaobaokeWidgetUrlConvertResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(url,"url");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
