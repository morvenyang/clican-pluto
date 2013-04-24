package com.taobao.api.internal.jushita;

import java.util.Map;

import com.taobao.api.ApiException;
import com.taobao.api.ApiRuleException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.internal.util.TaobaoHashMap;

/**
 * 聚石塔专用TOP客户端。
 * 
 * @author fengsheng
 * @since 1.0, May 22, 2012
 */
public class JushitaTaobaoClient {

	private static final String SYNC_CENTER_URL = "http://eai.taobao.com/api";

	private DefaultTaobaoClient client;

	public JushitaTaobaoClient(String serverUrl, String appKey, String appSecret) {
		this.client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
		this.client.setNeedCheckRequest(false);
		this.client.setNeedEnableParser(false);
	}

	public JushitaTaobaoClient(String appKey, String appSecret) {
		this(SYNC_CENTER_URL, appKey, appSecret);
	}

	public JushitaTaobaoClient(String serverUrl, String appKey, String appSecret, int connectTimeout, int readTimeout) {
		this.client = new DefaultTaobaoClient(serverUrl, appKey, appSecret, null, connectTimeout, readTimeout);
		this.client.setNeedCheckRequest(false);
		this.client.setNeedEnableParser(false);
	}

	public JushitaTaobaoClient(String serverUrl, String appKey, String appSecret,String format ,int connectTimeout, int readTimeout) {
		this.client = new DefaultTaobaoClient(serverUrl, appKey, appSecret, format, connectTimeout, readTimeout);
		this.client.setNeedCheckRequest(false);
		this.client.setNeedEnableParser(false);
	}
	public JushitaTaobaoClient(String serverUrl, String appKey, String appSecret,String format ,int connectTimeout, int readTimeout,String signMethod) {
		this.client = new DefaultTaobaoClient(serverUrl, appKey, appSecret, format, connectTimeout, readTimeout,signMethod);
		this.client.setNeedCheckRequest(false);
		this.client.setNeedEnableParser(false);
	}
	public String execute(String apiName, Map<String, String> params, String session) throws ApiException {
		JushitaRequest request = new JushitaRequest(apiName, params);
		JushitaResponse response = this.client.execute(request, session);
		return response.getBody();
	}

	public static class JushitaRequest implements TaobaoRequest<JushitaResponse> {
		private String apiName;
		private Map<String, String> params;
		private Map<String,String> headerMap=new TaobaoHashMap();
		public JushitaRequest(String apiName, Map<String, String> params) {
			this.apiName = apiName;
			this.params = params;
		}

		public String getApiMethodName() {
			return this.apiName;
		}

		public Map<String, String> getTextParams() {
			return this.params;
		}

		public Long getTimestamp() {
			return System.currentTimeMillis();
		}

		public void setTimestamp(Long timestamp) {
		}

		public Class<JushitaResponse> getResponseClass() {
			return JushitaResponse.class;
		}

		public void check() throws ApiRuleException {
		}

		public Map<String,String> getHeaderMap() {
			return headerMap;
		}
	}

	public static class JushitaResponse extends TaobaoResponse {
		private static final long serialVersionUID = -4167445591102791347L;
	}

}
