package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FenxiaoProductPduUpdateResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.fenxiao.product.pdu.update request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class FenxiaoProductPduUpdateRequest implements TaobaoRequest<FenxiaoProductPduUpdateResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 分销商ID
	 */
	private Long distributorId;

	/** 
	* 是否删除，删除指定分销商的数据
	 */
	private Boolean isDelete;

	/** 
	* 产品ID
	 */
	private Long productId;

	/** 
	* 库存是追加还是覆盖；删除操作可不传
append - 追加、overwrite - 覆盖
	 */
	private String quantityType;

	/** 
	* 0-999999的整数，可传入多个，以逗号隔开，顺序与属性列表保持一致；删除操作可不传
	 */
	private String quantitys;

	/** 
	* 产品包含sku时必选，可传入多个，以逗号隔开；删除操作可不传
	 */
	private String skuProperties;

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}
	public Long getDistributorId() {
		return this.distributorId;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public Boolean getIsDelete() {
		return this.isDelete;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getProductId() {
		return this.productId;
	}

	public void setQuantityType(String quantityType) {
		this.quantityType = quantityType;
	}
	public String getQuantityType() {
		return this.quantityType;
	}

	public void setQuantitys(String quantitys) {
		this.quantitys = quantitys;
	}
	public String getQuantitys() {
		return this.quantitys;
	}

	public void setSkuProperties(String skuProperties) {
		this.skuProperties = skuProperties;
	}
	public String getSkuProperties() {
		return this.skuProperties;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.fenxiao.product.pdu.update";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("distributor_id", this.distributorId);
		txtParams.put("is_delete", this.isDelete);
		txtParams.put("product_id", this.productId);
		txtParams.put("quantity_type", this.quantityType);
		txtParams.put("quantitys", this.quantitys);
		txtParams.put("sku_properties", this.skuProperties);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public void putOtherTextParam(String key, String value) {
		if(this.udfParams == null) {
			this.udfParams = new TaobaoHashMap();
		}
		this.udfParams.put(key, value);
	}

	public Class<FenxiaoProductPduUpdateResponse> getResponseClass() {
		return FenxiaoProductPduUpdateResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(distributorId,"distributorId");
		RequestCheckUtils.checkNotEmpty(productId,"productId");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
