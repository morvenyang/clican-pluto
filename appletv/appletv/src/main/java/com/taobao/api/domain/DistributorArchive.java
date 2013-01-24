package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 分销商档案信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class DistributorArchive extends TaobaoObject {

	private static final long serialVersionUID = 2472839984376665983L;

	/**
	 * 供应商授权分销商的产品的下载率。
率的值都是*10000后的，取值后直接除以100后加上%即可。比如12.33%，返回值是1233。
	 */
	@ApiField("down_load_ratio")
	private String downLoadRatio;

	/**
	 * 供应商在分销商店铺中的成交（已付款）订单笔数占比。
率的值都是*10000后的，取值后直接除以100后加上%即可。比如12.33%，返回值是1233。
	 */
	@ApiField("order_shop_ratio")
	private String orderShopRatio;

	/**
	 * 供应商授权分销商的产品的上架率。
率的值都是*10000后的，取值后直接除以100后加上%即可。比如12.33%，返回值是1233。
	 */
	@ApiField("up_self_ratio")
	private String upSelfRatio;

	/**
	 * 供应商在分销商店铺中的上架商品占比。
率的值都是*10000后的，取值后直接除以100后加上%即可。比如12.33%，返回值是1233。
	 */
	@ApiField("up_shop_ratio")
	private String upShopRatio;

	/**
	 * 供应商在分销商店铺中铺货商品UV占店铺商品总UV的比。
率的值都是*10000后的，取值后直接除以100后加上%即可。比如12.33%，返回值是1233。
	 */
	@ApiField("uv_shop_ratio")
	private String uvShopRatio;

	public String getDownLoadRatio() {
		return this.downLoadRatio;
	}
	public void setDownLoadRatio(String downLoadRatio) {
		this.downLoadRatio = downLoadRatio;
	}

	public String getOrderShopRatio() {
		return this.orderShopRatio;
	}
	public void setOrderShopRatio(String orderShopRatio) {
		this.orderShopRatio = orderShopRatio;
	}

	public String getUpSelfRatio() {
		return this.upSelfRatio;
	}
	public void setUpSelfRatio(String upSelfRatio) {
		this.upSelfRatio = upSelfRatio;
	}

	public String getUpShopRatio() {
		return this.upShopRatio;
	}
	public void setUpShopRatio(String upShopRatio) {
		this.upShopRatio = upShopRatio;
	}

	public String getUvShopRatio() {
		return this.uvShopRatio;
	}
	public void setUvShopRatio(String uvShopRatio) {
		this.uvShopRatio = uvShopRatio;
	}

}
