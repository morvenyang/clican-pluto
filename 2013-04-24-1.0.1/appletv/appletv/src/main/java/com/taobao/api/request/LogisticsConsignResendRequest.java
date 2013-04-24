package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.LogisticsConsignResendResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.logistics.consign.resend request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class LogisticsConsignResendRequest implements TaobaoRequest<LogisticsConsignResendResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 物流公司代码.如"POST"就代表中国邮政,"ZJS"就代表宅急送.调用 taobao.logistics.companies.get 获取。
<br><font color='red'>如果是货到付款订单，选择的物流公司必须支持货到付款发货方式</font>
	 */
	private String companyCode;

	/** 
	* feature参数格式<br>
范例: identCode=tid1:识别码1,识别码2|tid2:识别码3;machineCode=tid3:3C机器号A,3C机器号B<br>
identCode为识别码的KEY,machineCode为3C的KEY,多个key之间用”;”分隔<br>
“tid1:识别码1,识别码2|tid2:识别码3”为identCode对应的value。
"|"不同商品间的分隔符。<br>
例1商品和2商品，之间就用"|"分开。<br>
TID就是商品代表的子订单号，对应taobao.trade.fullinfo.get 接口获得的oid字段。(通过OID可以唯一定位到当前商品上)<br>
":"TID和具体传入参数间的分隔符。冒号前表示TID,之后代表该商品的参数属性。<br>
"," 属性间分隔符。（对应商品数量，当存在一个商品的数量超过1个时，用逗号分开）。<br>
具体:当订单中A商品的数量为2个，其中手机串号分别为"12345","67890"。<br>
参数格式：identCode=TIDA:12345,67890。
TIDA对应了A宝贝，冒号后用逗号分隔的"12345","67890".说明本订单A宝贝的数量为2，值分别为"12345","67890"。<br>
当存在"|"时，就说明订单中存在多个商品，商品间用"|"分隔了开来。|"之后的内容含义同上。
	 */
	private String feature;

	/** 
	* 运单号.具体一个物流公司的真实运单号码。淘宝官方物流会校验，请谨慎传入；
	 */
	private String outSid;

	/** 
	* 淘宝交易ID
	 */
	private Long tid;

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyCode() {
		return this.companyCode;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getFeature() {
		return this.feature;
	}

	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}
	public String getOutSid() {
		return this.outSid;
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
		return "taobao.logistics.consign.resend";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("company_code", this.companyCode);
		txtParams.put("feature", this.feature);
		txtParams.put("out_sid", this.outSid);
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

	public Class<LogisticsConsignResendResponse> getResponseClass() {
		return LogisticsConsignResendResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(companyCode,"companyCode");
		RequestCheckUtils.checkNotEmpty(outSid,"outSid");
		RequestCheckUtils.checkNotEmpty(tid,"tid");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
