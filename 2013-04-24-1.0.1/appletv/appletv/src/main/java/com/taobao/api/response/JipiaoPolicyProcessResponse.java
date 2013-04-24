package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.jipiao.policy.process response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class JipiaoPolicyProcessResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4444573665423253375L;

	/** 
	 * 机票政策添加返回状态，true，成功；false，失败
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/** 
	 * 返回政策主键id(机票系统唯一id)
	 */
	@ApiField("policy_id")
	private Long policyId;

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Boolean getIsSuccess( ) {
		return this.isSuccess;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}
	public Long getPolicyId( ) {
		return this.policyId;
	}

}
