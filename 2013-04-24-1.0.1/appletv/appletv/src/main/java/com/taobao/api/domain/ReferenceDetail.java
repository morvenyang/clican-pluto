package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 图片的引用详情
 *
 * @author auto create
 * @since 1.0, null
 */
public class ReferenceDetail extends TaobaoObject {

	private static final long serialVersionUID = 5492534375879626187L;

	/**
	 * 引用者的地址
	 */
	@ApiField("address")
	private String address;

	/**
	 * 引用者的名字
	 */
	@ApiField("name")
	private String name;

	/**
	 * 引用的类型:item,被宝贝引用；decorating，被装修引用；huabao，被画报引用；unreferenced，未被引用
	 */
	@ApiField("reference_type")
	private String referenceType;

	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getReferenceType() {
		return this.referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

}
