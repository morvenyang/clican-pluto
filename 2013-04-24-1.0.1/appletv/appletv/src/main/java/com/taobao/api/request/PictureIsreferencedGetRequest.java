package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.PictureIsreferencedGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.picture.isreferenced.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class PictureIsreferencedGetRequest implements TaobaoRequest<PictureIsreferencedGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 图片id
	 */
	private Long pictureId;

	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}
	public Long getPictureId() {
		return this.pictureId;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.picture.isreferenced.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("picture_id", this.pictureId);
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

	public Class<PictureIsreferencedGetResponse> getResponseClass() {
		return PictureIsreferencedGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(pictureId,"pictureId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
