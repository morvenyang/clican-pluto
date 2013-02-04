package com.clican.appletv.core.model;

import com.taobao.api.domain.Sku;

public class TaobaoSku {

	private String[] keys;
	private String[] values;
	private Sku sku;
	
	public String[] getKeys() {
		return keys;
	}
	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public Sku getSku() {
		return sku;
	}
	public void setSku(Sku sku) {
		this.sku = sku;
	}
	
	
}
