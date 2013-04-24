package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 旺旺信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class WangwangInfo extends TaobaoObject {

	private static final long serialVersionUID = 2714999433793974472L;

	/**
	 * 旺旺类型。cntaobao：taobao旺旺；cnalichn：阿里巴巴旺旺。
	 */
	@ApiField("site")
	private String site;

	/**
	 * 旺旺id
	 */
	@ApiField("wangwang_id")
	private String wangwangId;

	public String getSite() {
		return this.site;
	}
	public void setSite(String site) {
		this.site = site;
	}

	public String getWangwangId() {
		return this.wangwangId;
	}
	public void setWangwangId(String wangwangId) {
		this.wangwangId = wangwangId;
	}

}
