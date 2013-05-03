package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 授权信息数据结构
 *
 * @author auto create
 * @since 1.0, null
 */
public class InventoryAuthorizeInfo extends TaobaoObject {

	private static final long serialVersionUID = 5417372195381318117L;

	/**
	 * 本次授权的授权结果码
	 */
	@ApiField("authorize_code")
	private String authorizeCode;

	/**
	 * 渠道标记
1 – 16 淘宝保留
17- 32 商家定义
	 */
	@ApiField("channel_flag")
	private Long channelFlag;

	/**
	 * 批量请求时列表的序号
	 */
	@ApiField("index")
	private Long index;

	/**
	 * 库存类型 0-10 淘宝保留
11-20 商家自定义
1：普通良品
2：损坏
3：冻结
10：质押
	 */
	@ApiField("inventory_type")
	private Long inventoryType;

	/**
	 * 独享时授权信息的用户昵称
	 */
	@ApiField("nick_name")
	private String nickName;

	/**
	 * 共享时授权对象的淘宝昵称列表，用;分割
	 */
	@ApiField("nick_name_list")
	private String nickNameList;

	/**
	 * 当前总共占用的库存数量
	 */
	@ApiField("occupy_quantity")
	private Long occupyQuantity;

	/**
	 * 当前还剩下库存数量
	 */
	@ApiField("quantity")
	private Long quantity;

	/**
	 * 授权给其他用户的配额库存数量
	 */
	@ApiField("quota_quantity")
	private Long quotaQuantity;

	/**
	 * 商品编码
	 */
	@ApiField("sc_item_code")
	private String scItemCode;

	/**
	 * 商品ID
	 */
	@ApiField("sc_item_id")
	private Long scItemId;

	/**
	 * 商家仓编号
	 */
	@ApiField("store_code")
	private String storeCode;

	public String getAuthorizeCode() {
		return this.authorizeCode;
	}
	public void setAuthorizeCode(String authorizeCode) {
		this.authorizeCode = authorizeCode;
	}

	public Long getChannelFlag() {
		return this.channelFlag;
	}
	public void setChannelFlag(Long channelFlag) {
		this.channelFlag = channelFlag;
	}

	public Long getIndex() {
		return this.index;
	}
	public void setIndex(Long index) {
		this.index = index;
	}

	public Long getInventoryType() {
		return this.inventoryType;
	}
	public void setInventoryType(Long inventoryType) {
		this.inventoryType = inventoryType;
	}

	public String getNickName() {
		return this.nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickNameList() {
		return this.nickNameList;
	}
	public void setNickNameList(String nickNameList) {
		this.nickNameList = nickNameList;
	}

	public Long getOccupyQuantity() {
		return this.occupyQuantity;
	}
	public void setOccupyQuantity(Long occupyQuantity) {
		this.occupyQuantity = occupyQuantity;
	}

	public Long getQuantity() {
		return this.quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getQuotaQuantity() {
		return this.quotaQuantity;
	}
	public void setQuotaQuantity(Long quotaQuantity) {
		this.quotaQuantity = quotaQuantity;
	}

	public String getScItemCode() {
		return this.scItemCode;
	}
	public void setScItemCode(String scItemCode) {
		this.scItemCode = scItemCode;
	}

	public Long getScItemId() {
		return this.scItemId;
	}
	public void setScItemId(Long scItemId) {
		this.scItemId = scItemId;
	}

	public String getStoreCode() {
		return this.storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

}
