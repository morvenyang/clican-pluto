package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * Widget使用的sku属性对应信息结构体
 *
 * @author auto create
 * @since 1.0, null
 */
public class WidgetSkuProps extends TaobaoObject {

	private static final long serialVersionUID = 3672113621395368972L;

	/**
	 * 商品的属性值别名，对应商品中的property_alias字段中的别名部分
	 */
	@ApiField("alias")
	private String alias;

	/**
	 * 属性id对应的属性名称，对应类目属性中的属性名称
	 */
	@ApiField("key_name")
	private String keyName;

	/**
	 * 商品属性图片地址，对应Item中的propImgs，卖家设置了商品属性图片就有此字段，未设置此字段为空
	 */
	@ApiField("pic_url")
	private String picUrl;

	/**
	 * 淘宝的属性id，对应类目属性中的pid
	 */
	@ApiField("prop_key")
	private Long propKey;

	/**
	 * 淘宝的属性值id，对应类目属性中的vid
	 */
	@ApiField("prop_value")
	private Long propValue;

	/**
	 * 属性值id对应的属性标准名称，对应类目属性中的属性值名称
	 */
	@ApiField("value_name")
	private String valueName;

	public String getAlias() {
		return this.alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getKeyName() {
		return this.keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getPicUrl() {
		return this.picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Long getPropKey() {
		return this.propKey;
	}
	public void setPropKey(Long propKey) {
		this.propKey = propKey;
	}

	public Long getPropValue() {
		return this.propValue;
	}
	public void setPropValue(Long propValue) {
		this.propValue = propValue;
	}

	public String getValueName() {
		return this.valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

}
