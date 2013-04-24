package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.InventoryAuthorizeInfo;
import com.taobao.api.domain.TipInfo;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.inventory.authorize.set response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class InventoryAuthorizeSetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5286437926666187582L;

	/** 
	 * 授权结果
	 */
	@ApiListField("authorize_results")
	@ApiField("inventory_authorize_info")
	private List<InventoryAuthorizeInfo> authorizeResults;

	/** 
	 * 提示信息
	 */
	@ApiListField("tip_infos")
	@ApiField("tip_info")
	private List<TipInfo> tipInfos;

	public void setAuthorizeResults(List<InventoryAuthorizeInfo> authorizeResults) {
		this.authorizeResults = authorizeResults;
	}
	public List<InventoryAuthorizeInfo> getAuthorizeResults( ) {
		return this.authorizeResults;
	}

	public void setTipInfos(List<TipInfo> tipInfos) {
		this.tipInfos = tipInfos;
	}
	public List<TipInfo> getTipInfos( ) {
		return this.tipInfos;
	}

}
