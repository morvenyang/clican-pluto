package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.SimbaKeywordsAddResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.simba.keywords.add request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class SimbaKeywordsAddRequest implements TaobaoRequest<SimbaKeywordsAddResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 推广组id
	 */
	private Long adgroupId;

	/** 
	* 关键词，出价字符串和匹配方式字符串数组，最多100个;每个字符串：word+  ”^^”+price+”^^”+matchscope,
Price是整数，以“分”为单位，不能小于5，不能大于日限额; 
price为0则设置为使用默认出价；
matchscope只能是1,2,4（1代表精确匹配，2代表子串匹配，4代表广泛匹配）可不传。
关键词不能包含字符”^^”和 ”,”
	 */
	private String keywordPrices;

	/** 
	* 主人昵称
	 */
	private String nick;

	public void setAdgroupId(Long adgroupId) {
		this.adgroupId = adgroupId;
	}
	public Long getAdgroupId() {
		return this.adgroupId;
	}

	public void setKeywordPrices(String keywordPrices) {
		this.keywordPrices = keywordPrices;
	}
	public String getKeywordPrices() {
		return this.keywordPrices;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.simba.keywords.add";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("adgroup_id", this.adgroupId);
		txtParams.put("keyword_prices", this.keywordPrices);
		txtParams.put("nick", this.nick);
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

	public Class<SimbaKeywordsAddResponse> getResponseClass() {
		return SimbaKeywordsAddResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(adgroupId,"adgroupId");
		RequestCheckUtils.checkNotEmpty(keywordPrices,"keywordPrices");
		RequestCheckUtils.checkMaxListSize(keywordPrices,100,"keywordPrices");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
