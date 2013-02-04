package com.clican.appletv.core.model;

import java.util.List;

public class TaobaoSkuCategory {

	private String name;
	
	private List<TaobaoSku> skuList;
	
	private TaobaoSku selectedSku;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TaobaoSku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<TaobaoSku> skuList) {
		this.skuList = skuList;
	}

	public TaobaoSku getSelectedSku() {
		return selectedSku;
	}

	public void setSelectedSku(TaobaoSku selectedSku) {
		this.selectedSku = selectedSku;
	}
	
	
}
