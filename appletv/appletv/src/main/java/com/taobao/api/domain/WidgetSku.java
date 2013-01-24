package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 组件sku信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class WidgetSku extends TaobaoObject {

	private static final long serialVersionUID = 7813841515775481388L;

	/**
	 * sku的价格，对应Sku的price字段
	 */
	@ApiField("price")
	private String price;

	/**
	 * sku的属性串，根据pid的大小从小到大排列，前后都加";"。类型Sku的properties字段
	 */
	@ApiField("props")
	private String props;

	/**
	 * sku的库存数量，对应Sku的quantity字段
	 */
	@ApiField("quantity")
	private Long quantity;

	/**
	 * sku的id，对应Sku的sku_id字段
	 */
	@ApiField("sku_id")
	private Long skuId;

	public String getPrice() {
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public String getProps() {
		return this.props;
	}
	public void setProps(String props) {
		this.props = props;
	}

	public Long getQuantity() {
		return this.quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getSkuId() {
		return this.skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

}
