package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.TipInfo;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.inventory.occupy.apply response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class InventoryOccupyApplyResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4894437457624589415L;

	/** 
	 * 操作返回码
	 */
	@ApiField("operate_code")
	private String operateCode;

	/** 
	 * 提示信息, 
如果错误为后端商品不存在，存储错误的商品ID 
如果错误为库存不足，存储商品的对应库存数 
如果为强制最大化占用，存储商品实际占用库存数
	 */
	@ApiListField("tip_infos")
	@ApiField("tip_info")
	private List<TipInfo> tipInfos;

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}
	public String getOperateCode( ) {
		return this.operateCode;
	}

	public void setTipInfos(List<TipInfo> tipInfos) {
		this.tipInfos = tipInfos;
	}
	public List<TipInfo> getTipInfos( ) {
		return this.tipInfos;
	}

}
