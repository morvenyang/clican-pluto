package com.clican.appletv.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;

public class TaobaoSkuCache {

	private Item item;

	private List<String> skuLabelList;

	private Map<String, Set<TaobaoSkuValue>> skuLabelValueMap;

	private Map<String, Object> skuRelationMap;

	private String[] selectedValues;

	private String selectedValueString;

	private Map<String, List<TaobaoSkuValue>> skuDisplayLabelValueMap;

	private Map<String, Sku> skuMap;

	private Sku selectedSku;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Map<String, Sku> getSkuMap() {
		return skuMap;
	}

	public void setSkuMap(Map<String, Sku> skuMap) {
		this.skuMap = skuMap;
	}

	public List<String> getSkuLabelList() {
		return skuLabelList;
	}

	public void setSkuLabelList(List<String> skuLabelList) {
		this.skuLabelList = skuLabelList;
	}

	public Map<String, Set<TaobaoSkuValue>> getSkuLabelValueMap() {
		return skuLabelValueMap;
	}

	public void setSkuLabelValueMap(
			Map<String, Set<TaobaoSkuValue>> skuLabelValueMap) {
		this.skuLabelValueMap = skuLabelValueMap;
	}

	public Map<String, Object> getSkuRelationMap() {
		return skuRelationMap;
	}

	public void setSkuRelationMap(Map<String, Object> skuRelationMap) {
		this.skuRelationMap = skuRelationMap;
	}

	public String[] getSelectedValues() {
		return selectedValues;
	}

	public void setSelectedValues(String[] selectedValues) {
		this.selectedValues = selectedValues;
	}

	@SuppressWarnings("unchecked")
	public void updateSelectedValues(String selectedValue) {
		int index = -1;
		if (StringUtils.isNotEmpty(selectedValue)) {
			int offset = selectedValue.indexOf(":");
			index = Integer.parseInt(selectedValue.substring(0, offset));
			selectedValue = selectedValue.substring(offset + 1);
		}
		if (selectedValues == null) {
			selectedValues = new String[skuLabelList.size()];
		}
		for (int i = 0; i < skuLabelList.size(); i++) {
			if (i == index) {
				selectedValues[i] = selectedValue;
			} else {
				if (index == -1 || selectedValues[i] == null) {
					TaobaoSkuValue tsv = skuLabelValueMap
							.get(skuLabelList.get(i)).iterator().next();
					if (tsv != null) {
						selectedValues[i] = tsv.getValue();
					}
				}
			}
		}
		skuDisplayLabelValueMap = new HashMap<String, List<TaobaoSkuValue>>();
		for (int i = 0; i < selectedValues.length; i++) {
			String label = skuLabelList.get(i);
			List<TaobaoSkuValue> valueList = new ArrayList<TaobaoSkuValue>();
			skuDisplayLabelValueMap.put(label, valueList);
			Set<TaobaoSkuValue> valueSet = skuLabelValueMap.get(label);
			if (i == 0) {
				valueList.addAll(valueSet);
			} else {
				for (TaobaoSkuValue value : valueSet) {
					String prevalue = selectedValues[i - 1];
					Map<String, Object> relMap = (Map<String, Object>) skuRelationMap
							.get(prevalue);
					if (relMap.keySet().contains(value.getValue())) {
						valueList.add(value);
					}
				}
			}

		}

		this.selectedValueString = StringUtils.join(this.selectedValues, ";");
		this.selectedSku = skuMap.get(this.selectedValueString);
	}

	public Sku getSelectedSku() {
		return selectedSku;
	}

	public void setSelectedSku(Sku selectedSku) {
		this.selectedSku = selectedSku;
	}

	public Map<String, List<TaobaoSkuValue>> getSkuDisplayLabelValueMap() {
		return skuDisplayLabelValueMap;
	}

	public void setSkuDisplayLabelValueMap(
			Map<String, List<TaobaoSkuValue>> skuDisplayLabelValueMap) {
		this.skuDisplayLabelValueMap = skuDisplayLabelValueMap;
	}

	public String getSelectedValueString() {
		return selectedValueString;
	}

	public void setSelectedValueString(String selectedValueString) {
		this.selectedValueString = selectedValueString;
	}

}
