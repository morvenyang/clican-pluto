package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.AlibabaLogisticsOrderChargeResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: alibaba.logistics.order.charge request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class AlibabaLogisticsOrderChargeRequest implements TaobaoRequest<AlibabaLogisticsOrderChargeResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 货物描述
	 */
	private String cargoDescription;

	/** 
	* 货物名称
	 */
	private String cargoName;

	/** 
	* 付款方式。0：发货人支付；1：收货人支付；2：双方支付
	 */
	private String payType;

	/** 
	* 线路标志
	 */
	private String routeCode;

	/** 
	* 货物件数
	 */
	private Long totalNumber;

	/** 
	* 货物体积
	 */
	private String totalVolume;

	/** 
	* 货物重量
	 */
	private String totalWeight;

	/** 
	* 下单选中的增值服务
	 */
	private String vasList;

	public void setCargoDescription(String cargoDescription) {
		this.cargoDescription = cargoDescription;
	}
	public String getCargoDescription() {
		return this.cargoDescription;
	}

	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	public String getCargoName() {
		return this.cargoName;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayType() {
		return this.payType;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	public String getRouteCode() {
		return this.routeCode;
	}

	public void setTotalNumber(Long totalNumber) {
		this.totalNumber = totalNumber;
	}
	public Long getTotalNumber() {
		return this.totalNumber;
	}

	public void setTotalVolume(String totalVolume) {
		this.totalVolume = totalVolume;
	}
	public String getTotalVolume() {
		return this.totalVolume;
	}

	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}
	public String getTotalWeight() {
		return this.totalWeight;
	}

	public void setVasList(String vasList) {
		this.vasList = vasList;
	}
	public String getVasList() {
		return this.vasList;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "alibaba.logistics.order.charge";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cargo_description", this.cargoDescription);
		txtParams.put("cargo_name", this.cargoName);
		txtParams.put("pay_type", this.payType);
		txtParams.put("route_code", this.routeCode);
		txtParams.put("total_number", this.totalNumber);
		txtParams.put("total_volume", this.totalVolume);
		txtParams.put("total_weight", this.totalWeight);
		txtParams.put("vas_list", this.vasList);
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

	public Class<AlibabaLogisticsOrderChargeResponse> getResponseClass() {
		return AlibabaLogisticsOrderChargeResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(cargoName,"cargoName");
		RequestCheckUtils.checkNotEmpty(payType,"payType");
		RequestCheckUtils.checkNotEmpty(routeCode,"routeCode");
		RequestCheckUtils.checkNotEmpty(totalNumber,"totalNumber");
		RequestCheckUtils.checkMaxListSize(vasList,12,"vasList");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
