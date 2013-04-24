package com.taobao.api.domain;

import java.util.Date;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 淘宝商品
 *
 * @author auto create
 * @since 1.0, null
 */
public class SimbaItem extends TaobaoObject {

	private static final long serialVersionUID = 3322642482462273761L;

	/**
	 * 主人昵称
	 */
	@ApiField("nick")
	private String nick;

	/**
	 * 商品信息在外部系统(淘宝主站)的数字id
	 */
	@ApiField("num_id")
	private Long numId;

	/**
	 * 商品信息在外部系统（淘宝主站）的价格
	 */
	@ApiField("price")
	private String price;

	/**
	 * 发布时间
	 */
	@ApiField("publish_time")
	private Date publishTime;

	/**
	 * 库存
	 */
	@ApiField("quantity")
	private Long quantity;

	/**
	 * 销量
	 */
	@ApiField("sales_count")
	private Long salesCount;

	/**
	 * 商品信息在外部系统（淘宝主站）的标题
	 */
	@ApiField("title")
	private String title;

	public String getNick() {
		return this.nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	public Long getNumId() {
		return this.numId;
	}
	public void setNumId(Long numId) {
		this.numId = numId;
	}

	public String getPrice() {
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public Date getPublishTime() {
		return this.publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Long getQuantity() {
		return this.quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getSalesCount() {
		return this.salesCount;
	}
	public void setSalesCount(Long salesCount) {
		this.salesCount = salesCount;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
