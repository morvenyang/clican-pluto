package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 天猫精选商品列表
 *
 * @author auto create
 * @since 1.0, null
 */
public class SelectedItem extends TaobaoObject {

	private static final long serialVersionUID = 5355932121979492282L;

	/**
	 * 商品对应的后台类目id
	 */
	@ApiField("cid")
	private Long cid;

	/**
	 * 商品综合得分，根据商家运营能力、商家服务能力、商品质量多方面表现综合得到的分数。得分越高越好。
	 */
	@ApiField("item_score")
	private String itemScore;

	/**
	 * 店铺id
	 */
	@ApiField("shop_id")
	private Long shopId;

	/**
	 * 商品id（具有跟踪效果）代替原来的num_iid  <a href="http://dev.open.taobao.com/bbs/read.php?tid=24323">详细说明</a>
	 */
	@ApiField("track_iid")
	private String trackIid;

	public Long getCid() {
		return this.cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getItemScore() {
		return this.itemScore;
	}
	public void setItemScore(String itemScore) {
		this.itemScore = itemScore;
	}

	public Long getShopId() {
		return this.shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getTrackIid() {
		return this.trackIid;
	}
	public void setTrackIid(String trackIid) {
		this.trackIid = trackIid;
	}

}
