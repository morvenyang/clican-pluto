package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.Policy;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.jipiao.policy.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class JipiaoPolicyGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5888571886882311433L;

	/** 
	 * 机票政策查询返回结果对象
	 */
	@ApiField("policy")
	private Policy policy;

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public Policy getPolicy( ) {
		return this.policy;
	}

}
