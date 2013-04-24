package com.taobao.api.response;

import java.util.List;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.ItemProp;
import com.taobao.api.domain.PropValue;

import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.itemcats.increment.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class ItemcatsIncrementGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3521113988393663395L;

	/** 
	 * 增量类目信息
	 */
	@ApiListField("item_cats")
	@ApiField("item_cat")
	private List<ItemCat> itemCats;

	/** 
	 * 类目属性信息
	 */
	@ApiListField("item_props")
	@ApiField("item_prop")
	private List<ItemProp> itemProps;

	/** 
	 * 属性值
	 */
	@ApiListField("prop_values")
	@ApiField("prop_value")
	private List<PropValue> propValues;

	public void setItemCats(List<ItemCat> itemCats) {
		this.itemCats = itemCats;
	}
	public List<ItemCat> getItemCats( ) {
		return this.itemCats;
	}

	public void setItemProps(List<ItemProp> itemProps) {
		this.itemProps = itemProps;
	}
	public List<ItemProp> getItemProps( ) {
		return this.itemProps;
	}

	public void setPropValues(List<PropValue> propValues) {
		this.propValues = propValues;
	}
	public List<PropValue> getPropValues( ) {
		return this.propValues;
	}

}
