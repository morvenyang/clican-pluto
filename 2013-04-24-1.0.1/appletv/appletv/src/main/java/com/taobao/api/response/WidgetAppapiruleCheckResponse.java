package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.widget.appapirule.check response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class WidgetAppapiruleCheckResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3812312969491336224L;

	/** 
	 * 当前app是否可以调用次api。可以返回true，不可用返回false。
	 */
	@ApiField("call_permission")
	private String callPermission;

	/** 
	 * 此api请求的http类型：get或post
	 */
	@ApiField("http_method")
	private String httpMethod;

	/** 
	 * 此api是否需要用户授权。true表示必需授权，false表示可选授权或无需授权
	 */
	@ApiField("need_auth")
	private String needAuth;

	public void setCallPermission(String callPermission) {
		this.callPermission = callPermission;
	}
	public String getCallPermission( ) {
		return this.callPermission;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getHttpMethod( ) {
		return this.httpMethod;
	}

	public void setNeedAuth(String needAuth) {
		this.needAuth = needAuth;
	}
	public String getNeedAuth( ) {
		return this.needAuth;
	}

}
