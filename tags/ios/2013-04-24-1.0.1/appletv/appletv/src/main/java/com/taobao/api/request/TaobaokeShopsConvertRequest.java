package com.taobao.api.request;

import java.util.Map;

import com.taobao.api.ApiRuleException;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.response.TaobaokeShopsConvertResponse;

public class TaobaokeShopsConvertRequest implements TaobaoRequest<TaobaokeShopsConvertResponse>{

	@Override
	public String getApiMethodName() {
		return "taobao.taobaoke.shops.get";
	}

	@Override
	public Map<String, String> getTextParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTimestamp(Long timestamp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<TaobaokeShopsConvertResponse> getResponseClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void check() throws ApiRuleException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getHeaderMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
