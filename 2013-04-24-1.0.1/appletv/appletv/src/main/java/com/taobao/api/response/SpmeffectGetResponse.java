package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.SpmResult;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.spmeffect.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class SpmeffectGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4716792811186342484L;

	/** 
	 * 某一天该Appkey的站点导购数据
	 */
	@ApiField("spm_result")
	private SpmResult spmResult;

	public void setSpmResult(SpmResult spmResult) {
		this.spmResult = spmResult;
	}
	public SpmResult getSpmResult( ) {
		return this.spmResult;
	}

}
