package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.jipiao.policystatus.update response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class JipiaoPolicystatusUpdateResponse extends TaobaoResponse {

	private static final long serialVersionUID = 8428318529651897619L;

	/** 
	 * 机票政策状态更新结果，true，成功；false，失败
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Boolean getIsSuccess( ) {
		return this.isSuccess;
	}

}
