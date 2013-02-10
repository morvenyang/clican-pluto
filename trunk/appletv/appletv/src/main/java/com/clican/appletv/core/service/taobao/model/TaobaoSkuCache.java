package com.clican.appletv.core.service.taobao.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;

public class TaobaoSkuCache {

	private Item item;

	private String price;

	private List<String> skuLabelList;

	private Map<String, Set<TaobaoSkuValue>> skuLabelValueMap;

	private Map<String, Object> skuRelationMap;

	private String[] selectedValues;

	private String selectedValueString;

	private Map<String, List<TaobaoSkuValue>> skuDisplayLabelValueMap;

	private Map<String, Sku> skuMap;

	private Sku selectedSku;

	private TaobaoSkuPromotionWrap taobaoSkuPromotionWrap;

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
		if (index == -1) {
			List<String> all = new ArrayList<String>();
			for (int i = 0; i < skuLabelList.size(); i++) {
				List<String> temp = new ArrayList<String>();
				for (TaobaoSkuValue tsv : skuLabelValueMap.get(skuLabelList
						.get(i))) {
					if (all.size() == 0) {
						temp.add(tsv.getValue());
					} else {
						for (int j = 0; j < all.size(); j++) {
							temp.add(all.get(j) + ";" + tsv.getValue());
						}
					}
				}
				all = temp;
			}
			for (String svs : all) {
				this.selectedValueString = svs;
				this.selectedSku = skuMap.get(selectedValueString);
				if (this.selectedSku != null) {
					selectedValues = this.selectedValueString.split(";");
					break;
				}
			}
		} else {
			for (int i = 0; i < skuLabelList.size(); i++) {
				if (i == index) {
					selectedValues[i] = selectedValue;
				}
			}
			this.selectedValueString = StringUtils.join(selectedValues, ";");
			this.selectedSku = skuMap.get(selectedValueString);
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

		this.price = selectedSku.getPrice();
		if (this.getTaobaoSkuPromotionWrap() != null) {
			Set<String> keySet = this.getTaobaoSkuPromotionWrap()
					.getJsonObject().keySet();
			for (String key : keySet) {
				String[] ss = key.substring(1, key.length() - 1).split(";");
				List<String> list1 = new ArrayList<String>();
				for (String s : ss) {
					list1.add(s);
				}
				Collections.sort(list1);
				List<String> list2 = new ArrayList<String>();
				for (String s : selectedValues) {
					list2.add(s);
				}
				Collections.sort(list2);
				if (list1.equals(list2)) {
					JSONArray jsonArray = this.getTaobaoSkuPromotionWrap()
							.getJsonObject()
							.getJSONArray(key);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);
						String title = obj.getString("type");
						String price = null;
						if (obj.containsKey("price")) {
							price = obj.getString("price");
							TaobaoSkuPromotion promotion = new TaobaoSkuPromotion();
							promotion.setTitle(title);
							promotion.setPrice(price);
							this.price = title + " " + price;
							break;
						}
					}
					break;
				}
			}

		}
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

	public TaobaoSkuPromotionWrap getTaobaoSkuPromotionWrap() {
		return taobaoSkuPromotionWrap;
	}

	public void setTaobaoSkuPromotionWrap(
			TaobaoSkuPromotionWrap taobaoSkuPromotionWrap) {
		this.taobaoSkuPromotionWrap = taobaoSkuPromotionWrap;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
