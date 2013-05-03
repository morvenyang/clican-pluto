package com.taobao.api.domain;

import java.util.Date;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 淘宝客报表成员
 *
 * @author auto create
 * @since 1.0, null
 */
public class TaobaokeReportMember extends TaobaoObject {

	private static final long serialVersionUID = 3566377381529492941L;

	/**
	 * 应用授权码
	 */
	@ApiField("app_key")
	private String appKey;

	/**
	 * 所购买商品的类目ID
	 */
	@ApiField("category_id")
	private Long categoryId;

	/**
	 * 所购买商品的类目名称
	 */
	@ApiField("category_name")
	private String categoryName;

	/**
	 * 用户获得的佣金
	 */
	@ApiField("commission")
	private String commission;

	/**
	 * 佣金比率。比如：0.01代表1%
	 */
	@ApiField("commission_rate")
	private String commissionRate;

	/**
	 * 订单创建时间
	 */
	@ApiField("create_time")
	private Date createTime;

	/**
	 * 商品成交数量
	 */
	@ApiField("item_num")
	private Long itemNum;

	/**
	 * 商品标题
	 */
	@ApiField("item_title")
	private String itemTitle;

	/**
	 * 商品ID
	 */
	@ApiField("num_iid")
	private Long numIid;

	/**
	 * 推广渠道
	 */
	@ApiField("outer_code")
	private String outerCode;

	/**
	 * 成交价格
	 */
	@ApiField("pay_price")
	private String payPrice;

	/**
	 * 成交时间
	 */
	@ApiField("pay_time")
	private Date payTime;

	/**
	 * 实际支付金额
	 */
	@ApiField("real_pay_fee")
	private String realPayFee;

	/**
	 * 卖家昵称
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 店铺名称
	 */
	@ApiField("shop_title")
	private String shopTitle;

	/**
	 * 淘宝交易号
	 */
	@ApiField("trade_id")
	private Long tradeId;

	/**
	 * 淘宝父交易号
	 */
	@ApiField("trade_parent_id")
	private Long tradeParentId;

	public String getAppKey() {
		return this.appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Long getCategoryId() {
		return this.categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCommission() {
		return this.commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getCommissionRate() {
		return this.commissionRate;
	}
	public void setCommissionRate(String commissionRate) {
		this.commissionRate = commissionRate;
	}

	public Date getCreateTime() {
		return this.createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getItemNum() {
		return this.itemNum;
	}
	public void setItemNum(Long itemNum) {
		this.itemNum = itemNum;
	}

	public String getItemTitle() {
		return this.itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public Long getNumIid() {
		return this.numIid;
	}
	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}

	public String getOuterCode() {
		return this.outerCode;
	}
	public void setOuterCode(String outerCode) {
		this.outerCode = outerCode;
	}

	public String getPayPrice() {
		return this.payPrice;
	}
	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public Date getPayTime() {
		return this.payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getRealPayFee() {
		return this.realPayFee;
	}
	public void setRealPayFee(String realPayFee) {
		this.realPayFee = realPayFee;
	}

	public String getSellerNick() {
		return this.sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getShopTitle() {
		return this.shopTitle;
	}
	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public Long getTradeId() {
		return this.tradeId;
	}
	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public Long getTradeParentId() {
		return this.tradeParentId;
	}
	public void setTradeParentId(Long tradeParentId) {
		this.tradeParentId = tradeParentId;
	}

}
