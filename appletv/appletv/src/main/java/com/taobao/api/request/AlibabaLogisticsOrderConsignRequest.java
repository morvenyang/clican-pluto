package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.AlibabaLogisticsOrderConsignResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: alibaba.logistics.order.consign request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class AlibabaLogisticsOrderConsignRequest implements TaobaoRequest<AlibabaLogisticsOrderConsignResponse> {

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
	* 物流订单id
	 */
	private Long orderId;

	/** 
	* 付款方式。0：发货人支付；1：收货人支付；2：双方支付
	 */
	private String payType;

	/** 
	* 收件人地址
	 */
	private String receiverAddress;

	/** 
	* 收件人地区编码
	 */
	private Long receiverAreaId;

	/** 
	* 收件人城市名
	 */
	private String receiverCityName;

	/** 
	* 收件人公司名称
	 */
	private String receiverCorpName;

	/** 
	* 收件人区名
	 */
	private String receiverCountyName;

	/** 
	* 收件人手机号
	 */
	private String receiverMobile;

	/** 
	* 收件人名
	 */
	private String receiverName;

	/** 
	* 收件人电话区号
	 */
	private String receiverPhoneAreaCode;

	/** 
	* 收件人电话号码
	 */
	private String receiverPhoneTel;

	/** 
	* 收件人电话分机号
	 */
	private String receiverPhoneTelExt;

	/** 
	* 收件人邮编
	 */
	private String receiverPostcode;

	/** 
	* 收件人省名
	 */
	private String receiverProvinceName;

	/** 
	* 收件人旺旺号
	 */
	private String receiverWangwangNo;

	/** 
	* 退货接收人地址
	 */
	private String refunderAddress;

	/** 
	* 退货接收人地区id
	 */
	private Long refunderAreaId;

	/** 
	* 退货接收人城市名
	 */
	private String refunderCityName;

	/** 
	* 退货接收人公司名称
	 */
	private String refunderCorpName;

	/** 
	* 退货接收人区名
	 */
	private String refunderCountyName;

	/** 
	* 退货接收人手机号码
	 */
	private String refunderMobile;

	/** 
	* 退货接收人姓名
	 */
	private String refunderName;

	/** 
	* 退货接收人电话区号
	 */
	private String refunderPhoneAreaCode;

	/** 
	* 退货接收人电话号码
	 */
	private String refunderPhoneTel;

	/** 
	* 退货接收人电话分机号
	 */
	private String refunderPhoneTelExt;

	/** 
	* 退货接收人邮编
	 */
	private String refunderPostcode;

	/** 
	* 退货接收人省名
	 */
	private String refunderProvinceName;

	/** 
	* 退货接收人旺旺id
	 */
	private String refunderWangwangNo;

	/** 
	* 发货备注
	 */
	private String remark;

	/** 
	* 线路code，线路的业务标识。
	 */
	private String routeCode;

	/** 
	* 发货人地址
	 */
	private String senderAddress;

	/** 
	* 发货地区编码
	 */
	private Long senderAreaId;

	/** 
	* 发货人城市名
	 */
	private String senderCityName;

	/** 
	* 发货人公司名称
	 */
	private String senderCorpName;

	/** 
	* 发货人区名
	 */
	private String senderCountyName;

	/** 
	* 发货人手机号
	 */
	private String senderMobile;

	/** 
	* 发货人姓名
	 */
	private String senderName;

	/** 
	* 发货人电话区号
	 */
	private String senderPhoneAreaCode;

	/** 
	* 发货人电话
	 */
	private String senderPhoneTel;

	/** 
	* 发货人电话分机号
	 */
	private String senderPhoneTelExt;

	/** 
	* 发货人地区邮编
	 */
	private String senderPostcode;

	/** 
	* 发货人省名
	 */
	private String senderProvinceName;

	/** 
	* 发货人旺旺号
	 */
	private String senderWangwangNo;

	/** 
	* top开放的来源。默认都使用：TAOBAO_TOP。
	 */
	private String source;

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

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getOrderId() {
		return this.orderId;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayType() {
		return this.payType;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverAddress() {
		return this.receiverAddress;
	}

	public void setReceiverAreaId(Long receiverAreaId) {
		this.receiverAreaId = receiverAreaId;
	}
	public Long getReceiverAreaId() {
		return this.receiverAreaId;
	}

	public void setReceiverCityName(String receiverCityName) {
		this.receiverCityName = receiverCityName;
	}
	public String getReceiverCityName() {
		return this.receiverCityName;
	}

	public void setReceiverCorpName(String receiverCorpName) {
		this.receiverCorpName = receiverCorpName;
	}
	public String getReceiverCorpName() {
		return this.receiverCorpName;
	}

	public void setReceiverCountyName(String receiverCountyName) {
		this.receiverCountyName = receiverCountyName;
	}
	public String getReceiverCountyName() {
		return this.receiverCountyName;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverMobile() {
		return this.receiverMobile;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverName() {
		return this.receiverName;
	}

	public void setReceiverPhoneAreaCode(String receiverPhoneAreaCode) {
		this.receiverPhoneAreaCode = receiverPhoneAreaCode;
	}
	public String getReceiverPhoneAreaCode() {
		return this.receiverPhoneAreaCode;
	}

	public void setReceiverPhoneTel(String receiverPhoneTel) {
		this.receiverPhoneTel = receiverPhoneTel;
	}
	public String getReceiverPhoneTel() {
		return this.receiverPhoneTel;
	}

	public void setReceiverPhoneTelExt(String receiverPhoneTelExt) {
		this.receiverPhoneTelExt = receiverPhoneTelExt;
	}
	public String getReceiverPhoneTelExt() {
		return this.receiverPhoneTelExt;
	}

	public void setReceiverPostcode(String receiverPostcode) {
		this.receiverPostcode = receiverPostcode;
	}
	public String getReceiverPostcode() {
		return this.receiverPostcode;
	}

	public void setReceiverProvinceName(String receiverProvinceName) {
		this.receiverProvinceName = receiverProvinceName;
	}
	public String getReceiverProvinceName() {
		return this.receiverProvinceName;
	}

	public void setReceiverWangwangNo(String receiverWangwangNo) {
		this.receiverWangwangNo = receiverWangwangNo;
	}
	public String getReceiverWangwangNo() {
		return this.receiverWangwangNo;
	}

	public void setRefunderAddress(String refunderAddress) {
		this.refunderAddress = refunderAddress;
	}
	public String getRefunderAddress() {
		return this.refunderAddress;
	}

	public void setRefunderAreaId(Long refunderAreaId) {
		this.refunderAreaId = refunderAreaId;
	}
	public Long getRefunderAreaId() {
		return this.refunderAreaId;
	}

	public void setRefunderCityName(String refunderCityName) {
		this.refunderCityName = refunderCityName;
	}
	public String getRefunderCityName() {
		return this.refunderCityName;
	}

	public void setRefunderCorpName(String refunderCorpName) {
		this.refunderCorpName = refunderCorpName;
	}
	public String getRefunderCorpName() {
		return this.refunderCorpName;
	}

	public void setRefunderCountyName(String refunderCountyName) {
		this.refunderCountyName = refunderCountyName;
	}
	public String getRefunderCountyName() {
		return this.refunderCountyName;
	}

	public void setRefunderMobile(String refunderMobile) {
		this.refunderMobile = refunderMobile;
	}
	public String getRefunderMobile() {
		return this.refunderMobile;
	}

	public void setRefunderName(String refunderName) {
		this.refunderName = refunderName;
	}
	public String getRefunderName() {
		return this.refunderName;
	}

	public void setRefunderPhoneAreaCode(String refunderPhoneAreaCode) {
		this.refunderPhoneAreaCode = refunderPhoneAreaCode;
	}
	public String getRefunderPhoneAreaCode() {
		return this.refunderPhoneAreaCode;
	}

	public void setRefunderPhoneTel(String refunderPhoneTel) {
		this.refunderPhoneTel = refunderPhoneTel;
	}
	public String getRefunderPhoneTel() {
		return this.refunderPhoneTel;
	}

	public void setRefunderPhoneTelExt(String refunderPhoneTelExt) {
		this.refunderPhoneTelExt = refunderPhoneTelExt;
	}
	public String getRefunderPhoneTelExt() {
		return this.refunderPhoneTelExt;
	}

	public void setRefunderPostcode(String refunderPostcode) {
		this.refunderPostcode = refunderPostcode;
	}
	public String getRefunderPostcode() {
		return this.refunderPostcode;
	}

	public void setRefunderProvinceName(String refunderProvinceName) {
		this.refunderProvinceName = refunderProvinceName;
	}
	public String getRefunderProvinceName() {
		return this.refunderProvinceName;
	}

	public void setRefunderWangwangNo(String refunderWangwangNo) {
		this.refunderWangwangNo = refunderWangwangNo;
	}
	public String getRefunderWangwangNo() {
		return this.refunderWangwangNo;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return this.remark;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	public String getRouteCode() {
		return this.routeCode;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getSenderAddress() {
		return this.senderAddress;
	}

	public void setSenderAreaId(Long senderAreaId) {
		this.senderAreaId = senderAreaId;
	}
	public Long getSenderAreaId() {
		return this.senderAreaId;
	}

	public void setSenderCityName(String senderCityName) {
		this.senderCityName = senderCityName;
	}
	public String getSenderCityName() {
		return this.senderCityName;
	}

	public void setSenderCorpName(String senderCorpName) {
		this.senderCorpName = senderCorpName;
	}
	public String getSenderCorpName() {
		return this.senderCorpName;
	}

	public void setSenderCountyName(String senderCountyName) {
		this.senderCountyName = senderCountyName;
	}
	public String getSenderCountyName() {
		return this.senderCountyName;
	}

	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}
	public String getSenderMobile() {
		return this.senderMobile;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderName() {
		return this.senderName;
	}

	public void setSenderPhoneAreaCode(String senderPhoneAreaCode) {
		this.senderPhoneAreaCode = senderPhoneAreaCode;
	}
	public String getSenderPhoneAreaCode() {
		return this.senderPhoneAreaCode;
	}

	public void setSenderPhoneTel(String senderPhoneTel) {
		this.senderPhoneTel = senderPhoneTel;
	}
	public String getSenderPhoneTel() {
		return this.senderPhoneTel;
	}

	public void setSenderPhoneTelExt(String senderPhoneTelExt) {
		this.senderPhoneTelExt = senderPhoneTelExt;
	}
	public String getSenderPhoneTelExt() {
		return this.senderPhoneTelExt;
	}

	public void setSenderPostcode(String senderPostcode) {
		this.senderPostcode = senderPostcode;
	}
	public String getSenderPostcode() {
		return this.senderPostcode;
	}

	public void setSenderProvinceName(String senderProvinceName) {
		this.senderProvinceName = senderProvinceName;
	}
	public String getSenderProvinceName() {
		return this.senderProvinceName;
	}

	public void setSenderWangwangNo(String senderWangwangNo) {
		this.senderWangwangNo = senderWangwangNo;
	}
	public String getSenderWangwangNo() {
		return this.senderWangwangNo;
	}

	public void setSource(String source) {
		this.source = source;
	}
	public String getSource() {
		return this.source;
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
		return "alibaba.logistics.order.consign";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("cargo_description", this.cargoDescription);
		txtParams.put("cargo_name", this.cargoName);
		txtParams.put("order_id", this.orderId);
		txtParams.put("pay_type", this.payType);
		txtParams.put("receiver_address", this.receiverAddress);
		txtParams.put("receiver_area_id", this.receiverAreaId);
		txtParams.put("receiver_city_name", this.receiverCityName);
		txtParams.put("receiver_corp_name", this.receiverCorpName);
		txtParams.put("receiver_county_name", this.receiverCountyName);
		txtParams.put("receiver_mobile", this.receiverMobile);
		txtParams.put("receiver_name", this.receiverName);
		txtParams.put("receiver_phone_area_code", this.receiverPhoneAreaCode);
		txtParams.put("receiver_phone_tel", this.receiverPhoneTel);
		txtParams.put("receiver_phone_tel_ext", this.receiverPhoneTelExt);
		txtParams.put("receiver_postcode", this.receiverPostcode);
		txtParams.put("receiver_province_name", this.receiverProvinceName);
		txtParams.put("receiver_wangwang_no", this.receiverWangwangNo);
		txtParams.put("refunder_address", this.refunderAddress);
		txtParams.put("refunder_area_id", this.refunderAreaId);
		txtParams.put("refunder_city_name", this.refunderCityName);
		txtParams.put("refunder_corp_name", this.refunderCorpName);
		txtParams.put("refunder_county_name", this.refunderCountyName);
		txtParams.put("refunder_mobile", this.refunderMobile);
		txtParams.put("refunder_name", this.refunderName);
		txtParams.put("refunder_phone_area_code", this.refunderPhoneAreaCode);
		txtParams.put("refunder_phone_tel", this.refunderPhoneTel);
		txtParams.put("refunder_phone_tel_ext", this.refunderPhoneTelExt);
		txtParams.put("refunder_postcode", this.refunderPostcode);
		txtParams.put("refunder_province_name", this.refunderProvinceName);
		txtParams.put("refunder_wangwang_no", this.refunderWangwangNo);
		txtParams.put("remark", this.remark);
		txtParams.put("route_code", this.routeCode);
		txtParams.put("sender_address", this.senderAddress);
		txtParams.put("sender_area_id", this.senderAreaId);
		txtParams.put("sender_city_name", this.senderCityName);
		txtParams.put("sender_corp_name", this.senderCorpName);
		txtParams.put("sender_county_name", this.senderCountyName);
		txtParams.put("sender_mobile", this.senderMobile);
		txtParams.put("sender_name", this.senderName);
		txtParams.put("sender_phone_area_code", this.senderPhoneAreaCode);
		txtParams.put("sender_phone_tel", this.senderPhoneTel);
		txtParams.put("sender_phone_tel_ext", this.senderPhoneTelExt);
		txtParams.put("sender_postcode", this.senderPostcode);
		txtParams.put("sender_province_name", this.senderProvinceName);
		txtParams.put("sender_wangwang_no", this.senderWangwangNo);
		txtParams.put("source", this.source);
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

	public Class<AlibabaLogisticsOrderConsignResponse> getResponseClass() {
		return AlibabaLogisticsOrderConsignResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(cargoName,"cargoName");
		RequestCheckUtils.checkNotEmpty(orderId,"orderId");
		RequestCheckUtils.checkNotEmpty(payType,"payType");
		RequestCheckUtils.checkNotEmpty(routeCode,"routeCode");
		RequestCheckUtils.checkNotEmpty(source,"source");
		RequestCheckUtils.checkNotEmpty(totalNumber,"totalNumber");
		RequestCheckUtils.checkMaxListSize(vasList,12,"vasList");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
