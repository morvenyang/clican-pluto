package com.taobao.api.domain;

import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * Widget获取到的商品信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class WidgetItem extends TaobaoObject {

	private static final long serialVersionUID = 7684226449421872481L;

	/**
	 * 该商品能否加入购物车
	 */
	@ApiField("add_to_cart")
	private Boolean addToCart;

	/**
	 * 商品的状态。onsale出售中，instock仓库中
	 */
	@ApiField("approve_status")
	private String approveStatus;

	/**
	 * 商品的点击链接，如果此商品有淘宝客会根据app所属的淘宝用户进行淘宝客连接转换，如果无淘宝客此字段为淘宝商品详情地址
	 */
	@ApiField("click_url")
	private String clickUrl;

	/**
	 * 是否商城的商品
	 */
	@ApiField("is_mall")
	private Boolean isMall;

	/**
	 * 淘宝商品的数字id，与Item的num_iid一致
	 */
	@ApiField("item_id")
	private Long itemId;

	/**
	 * 商品图片列表，对应Item的itemImgs
	 */
	@ApiListField("item_pics")
	@ApiField("string")
	private List<String> itemPics;

	/**
	 * 商品关联的商品优惠信息
	 */
	@ApiListField("item_promotion_data")
	@ApiField("promotion_in_item")
	private List<PromotionInItem> itemPromotionData;

	/**
	 * 商品的主图地址，对应Item的pic_url
	 */
	@ApiField("pic_url")
	private String picUrl;

	/**
	 * 淘宝商品的价格，对应Item的price。如果商品为无sku或者所有sku价格一致的商品，此字段为价格（199.99）；如果商品有多sku且有一个价格区间，次字段为商品的价格区间，中间用‘-’连接
	 */
	@ApiField("price")
	private String price;

	/**
	 * 商品的数量，对应Item的num
	 */
	@ApiField("quantity")
	private Long quantity;

	/**
	 * 商品卖家昵称，对应Item的nick
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 商品关联的卖家店铺优惠信息
	 */
	@ApiListField("shop_promotion_data")
	@ApiField("promotion_in_shop")
	private List<PromotionInShop> shopPromotionData;

	/**
	 * 商品关联sku对应的商品属性列表信息
	 */
	@ApiListField("sku_props")
	@ApiField("widget_sku_props")
	private List<WidgetSkuProps> skuProps;

	/**
	 * 商品关联的sku列表信息
	 */
	@ApiListField("skus")
	@ApiField("widget_sku")
	private List<WidgetSku> skus;

	/**
	 * 淘宝商品的标题，与Item的title一致
	 */
	@ApiField("title")
	private String title;

	public Boolean getAddToCart() {
		return this.addToCart;
	}
	public void setAddToCart(Boolean addToCart) {
		this.addToCart = addToCart;
	}

	public String getApproveStatus() {
		return this.approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getClickUrl() {
		return this.clickUrl;
	}
	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public Boolean getIsMall() {
		return this.isMall;
	}
	public void setIsMall(Boolean isMall) {
		this.isMall = isMall;
	}

	public Long getItemId() {
		return this.itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public List<String> getItemPics() {
		return this.itemPics;
	}
	public void setItemPics(List<String> itemPics) {
		this.itemPics = itemPics;
	}

	public List<PromotionInItem> getItemPromotionData() {
		return this.itemPromotionData;
	}
	public void setItemPromotionData(List<PromotionInItem> itemPromotionData) {
		this.itemPromotionData = itemPromotionData;
	}

	public String getPicUrl() {
		return this.picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPrice() {
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public Long getQuantity() {
		return this.quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getSellerNick() {
		return this.sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public List<PromotionInShop> getShopPromotionData() {
		return this.shopPromotionData;
	}
	public void setShopPromotionData(List<PromotionInShop> shopPromotionData) {
		this.shopPromotionData = shopPromotionData;
	}

	public List<WidgetSkuProps> getSkuProps() {
		return this.skuProps;
	}
	public void setSkuProps(List<WidgetSkuProps> skuProps) {
		this.skuProps = skuProps;
	}

	public List<WidgetSku> getSkus() {
		return this.skus;
	}
	public void setSkus(List<WidgetSku> skus) {
		this.skus = skus;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
