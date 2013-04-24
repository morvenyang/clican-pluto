package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.JipiaoPolicyProcessResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.jipiao.policy.process request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class JipiaoPolicyProcessRequest implements TaobaoRequest<JipiaoPolicyProcessResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 政策所支持的航空公司二字码
	 */
	private String airline;

	/** 
	* 政策支持的到达机场列表，逗号分隔的机场三字码，
注： 
1.让利(8)政策，当到达支持多个(最多25)时，出发只能支持一个； 
2.特价(6)/特殊(10)政策，出发/到达城市只支持一个
	 */
	private String arrAirports;

	/** 
	* 扩展字段：
    "rfc" 对应值 1:不退不改不签,2:收费退改签（退、改、签中任意一个收费即为收费退改签）3:免费退改签
    "ipt" 对应值 1:“打印价格等于销售价”,2:“不提供行程单”,3:“打印价格高于销售价”,4:“打印价格低于销售价”,例如：rfc=1;ipt=1
	 */
	private String attributes;

	/** 
	* 政策是否支持自动HK，默认为不支持
	 */
	private Boolean autoHkFlag;

	/** 
	* 政策是否支持自动出票，默认不支持，
注：自动出票时，必须同时自动HK
	 */
	private Boolean autoTicketFlag;

	/** 
	* @1: [{"matcher":{"mode":"ALL","list":["D"]},"priceStrategy":{"mode":
"TICKET_PRICE","modeBaseValue":500,"retention":500,"rebase":-5}}]

@2: [{"matcher"
:{"mode":"ALL","list":["D"]},"priceStrategy":{"mode":"DISCOUNT"
,"modeBaseValue"
:null,"retention":300,"rebase":-5}}]

@3: [{"matcher":{"mode":"ALL","list":["D"]},"priceStrategy":{"mode":"Y_DISCOUNT","modeBaseValue":75,"retention":500,"rebase":-5}}]

规则说明： cabin_rules分二个字段：matcher:适应舱位（又包含两个字段：mode：匹配模式枚举(填INCLUDE），list：适应舱位列表），priceStrategy:价格策略（mode:价格模式（让利产品：DISCOUNT，特价特殊产品有四种模式：票面价用TICKET_PRICE，Y舱折扣用Y_DISCOUNT，C舱折扣用C_DISCOUNT，F舱折扣用F_DISCOUNT）；modeBaseValue:当价格模式mode为DISCOUNT，modeBaseValue不填；当价格模式mode为TICKET_PRICE，modeBaseValue填票面价；当价格模式mode为Y_DISCOUNT/C_DISCOUNT/F_DISCOUNT，modeBaseValue填对应的折扣；retention为返点的百分比；rebase为留钱）
注意：特殊政策不需要舱位时需要传入22；retention、rebase传入小数时，会取整数部分
	 */
	private String cabinRules;

	/** 
	* 改签规则
	 */
	private String changeRule;

	/** 
	* 政策适用的星期几，1-7分别表示周一到周日
注：特殊政策不能填写该字段；其它政策填写时，
包含全部时需要设置为1234567
	 */
	private String dayOfWeeks;

	/** 
	* 政策支持的出发机场列表，逗号分隔的机场三字码，
注：
1.让利(8)政策，当出发支持多个(最多25)时，到达只能支持一个；
2.特价(6)/特殊(10)政策，出发/到达城市只支持一个
	 */
	private String depAirports;

	/** 
	* ei项，自动HK，自动出票设定时必需
	 */
	private String ei;

	/** 
	* 政策旅行有效日期中排除指定日期，设定日期将不在搜索结果页面展现
注：最多排除20天，特殊政策无此设置
	 */
	private String excludeDate;

	/** 
	* 政策销售日期最早提前天数，默认-1表示无效
	 */
	private Long firstSaleAdvanceDay;

	/** 
	* flags是二进制标志
从右到左数，第1个位表示：不PAT自动HK 
第2个位表示：儿童可与成人同价
比如：“儿童可与成人同价”=true ,“不PAT自动HK”=false,则表示成二进制字符串"10",换算成十进制flags=2
	 */
	private Long flags;

	/** 
	* 包含/排除的航班号，注意格式：
+CA1234,CZ3166，表示包含列表
-CA1234,CZ3166，表示排除列表
默认包含所有航班；
不支持同时包含和排除
	 */
	private String flightInfo;

	/** 
	* 政策销售日期最晚提前天数，默认-1表示无效
	 */
	private Long lastSaleAdvanceDay;

	/** 
	* 代理商对政策的备注信息
	 */
	private String memo;

	/** 
	* 政策设置为支持自动HK，自动出票时必需
	 */
	private String officeId;

	/** 
	* 政策的外部id，用于关联代理商自身维护的政策id，提供外部产品id时，可以在查询和修改时作为唯一条件使用
	 */
	private String outProductId;

	/** 
	* 0，type为0时，不需要设置；
1，type为1时，表示policy_id为政策id
2，type为2时，表示policy_id为政策out_product_id
	 */
	private String policyId;

	/** 
	* 政策类型：6，特价政策；8，让利政策；10，特殊政策
	 */
	private Long policyType;

	/** 
	* 退票规则
	 */
	private String refundRule;

	/** 
	* 签转规则
	 */
	private String reissueRule;

	/** 
	* 政策销售有效日期的截止日期(注意，格式如：20120-03-10 10:30:31 等价2012-03-11)
	 */
	private Date saleEndDate;

	/** 
	* 政策销售有效日期的开始日期(注意，格式如：20120-03-10 10:30:31 等价2012-03-11)
	 */
	private Date saleStartDate;

	/** 
	* 政策类型为10时，用于设置政策的每天的库存信息
	 */
	private String seatInfo;

	/** 
	* 政策是否支持共享航班，默认为不支持；设置不支持情况下，当航班的实际承运方不是当前航空公司，则搜索结果页不能展现
	 */
	private Boolean shareSupport;

	/** 
	* 特殊说明
	 */
	private String specialRule;

	/** 
	* 政策旅行有效日期的结束日期(注意，格式如：20120-03-10 10:30:31 等价2012-03-11)
	 */
	private Date travelEndDate;

	/** 
	* 政策旅行有效日期的开始日期(注意，格式，如：2012-03-10 10:30:31 等价2012-03-10)
	 */
	private Date travelStartDate;

	/** 
	* 0，表示添加政策；
1，表示按id修改政策；
2，表示按out_product_id修改政策
	 */
	private Long type;

	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getAirline() {
		return this.airline;
	}

	public void setArrAirports(String arrAirports) {
		this.arrAirports = arrAirports;
	}
	public String getArrAirports() {
		return this.arrAirports;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getAttributes() {
		return this.attributes;
	}

	public void setAutoHkFlag(Boolean autoHkFlag) {
		this.autoHkFlag = autoHkFlag;
	}
	public Boolean getAutoHkFlag() {
		return this.autoHkFlag;
	}

	public void setAutoTicketFlag(Boolean autoTicketFlag) {
		this.autoTicketFlag = autoTicketFlag;
	}
	public Boolean getAutoTicketFlag() {
		return this.autoTicketFlag;
	}

	public void setCabinRules(String cabinRules) {
		this.cabinRules = cabinRules;
	}
	public String getCabinRules() {
		return this.cabinRules;
	}

	public void setChangeRule(String changeRule) {
		this.changeRule = changeRule;
	}
	public String getChangeRule() {
		return this.changeRule;
	}

	public void setDayOfWeeks(String dayOfWeeks) {
		this.dayOfWeeks = dayOfWeeks;
	}
	public String getDayOfWeeks() {
		return this.dayOfWeeks;
	}

	public void setDepAirports(String depAirports) {
		this.depAirports = depAirports;
	}
	public String getDepAirports() {
		return this.depAirports;
	}

	public void setEi(String ei) {
		this.ei = ei;
	}
	public String getEi() {
		return this.ei;
	}

	public void setExcludeDate(String excludeDate) {
		this.excludeDate = excludeDate;
	}
	public String getExcludeDate() {
		return this.excludeDate;
	}

	public void setFirstSaleAdvanceDay(Long firstSaleAdvanceDay) {
		this.firstSaleAdvanceDay = firstSaleAdvanceDay;
	}
	public Long getFirstSaleAdvanceDay() {
		return this.firstSaleAdvanceDay;
	}

	public void setFlags(Long flags) {
		this.flags = flags;
	}
	public Long getFlags() {
		return this.flags;
	}

	public void setFlightInfo(String flightInfo) {
		this.flightInfo = flightInfo;
	}
	public String getFlightInfo() {
		return this.flightInfo;
	}

	public void setLastSaleAdvanceDay(Long lastSaleAdvanceDay) {
		this.lastSaleAdvanceDay = lastSaleAdvanceDay;
	}
	public Long getLastSaleAdvanceDay() {
		return this.lastSaleAdvanceDay;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemo() {
		return this.memo;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getOfficeId() {
		return this.officeId;
	}

	public void setOutProductId(String outProductId) {
		this.outProductId = outProductId;
	}
	public String getOutProductId() {
		return this.outProductId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	public String getPolicyId() {
		return this.policyId;
	}

	public void setPolicyType(Long policyType) {
		this.policyType = policyType;
	}
	public Long getPolicyType() {
		return this.policyType;
	}

	public void setRefundRule(String refundRule) {
		this.refundRule = refundRule;
	}
	public String getRefundRule() {
		return this.refundRule;
	}

	public void setReissueRule(String reissueRule) {
		this.reissueRule = reissueRule;
	}
	public String getReissueRule() {
		return this.reissueRule;
	}

	public void setSaleEndDate(Date saleEndDate) {
		this.saleEndDate = saleEndDate;
	}
	public Date getSaleEndDate() {
		return this.saleEndDate;
	}

	public void setSaleStartDate(Date saleStartDate) {
		this.saleStartDate = saleStartDate;
	}
	public Date getSaleStartDate() {
		return this.saleStartDate;
	}

	public void setSeatInfo(String seatInfo) {
		this.seatInfo = seatInfo;
	}
	public String getSeatInfo() {
		return this.seatInfo;
	}

	public void setShareSupport(Boolean shareSupport) {
		this.shareSupport = shareSupport;
	}
	public Boolean getShareSupport() {
		return this.shareSupport;
	}

	public void setSpecialRule(String specialRule) {
		this.specialRule = specialRule;
	}
	public String getSpecialRule() {
		return this.specialRule;
	}

	public void setTravelEndDate(Date travelEndDate) {
		this.travelEndDate = travelEndDate;
	}
	public Date getTravelEndDate() {
		return this.travelEndDate;
	}

	public void setTravelStartDate(Date travelStartDate) {
		this.travelStartDate = travelStartDate;
	}
	public Date getTravelStartDate() {
		return this.travelStartDate;
	}

	public void setType(Long type) {
		this.type = type;
	}
	public Long getType() {
		return this.type;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.jipiao.policy.process";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("airline", this.airline);
		txtParams.put("arr_airports", this.arrAirports);
		txtParams.put("attributes", this.attributes);
		txtParams.put("auto_hk_flag", this.autoHkFlag);
		txtParams.put("auto_ticket_flag", this.autoTicketFlag);
		txtParams.put("cabin_rules", this.cabinRules);
		txtParams.put("change_rule", this.changeRule);
		txtParams.put("day_of_weeks", this.dayOfWeeks);
		txtParams.put("dep_airports", this.depAirports);
		txtParams.put("ei", this.ei);
		txtParams.put("exclude_date", this.excludeDate);
		txtParams.put("first_sale_advance_day", this.firstSaleAdvanceDay);
		txtParams.put("flags", this.flags);
		txtParams.put("flight_info", this.flightInfo);
		txtParams.put("last_sale_advance_day", this.lastSaleAdvanceDay);
		txtParams.put("memo", this.memo);
		txtParams.put("office_id", this.officeId);
		txtParams.put("out_product_id", this.outProductId);
		txtParams.put("policy_id", this.policyId);
		txtParams.put("policy_type", this.policyType);
		txtParams.put("refund_rule", this.refundRule);
		txtParams.put("reissue_rule", this.reissueRule);
		txtParams.put("sale_end_date", this.saleEndDate);
		txtParams.put("sale_start_date", this.saleStartDate);
		txtParams.put("seat_info", this.seatInfo);
		txtParams.put("share_support", this.shareSupport);
		txtParams.put("special_rule", this.specialRule);
		txtParams.put("travel_end_date", this.travelEndDate);
		txtParams.put("travel_start_date", this.travelStartDate);
		txtParams.put("type", this.type);
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

	public Class<JipiaoPolicyProcessResponse> getResponseClass() {
		return JipiaoPolicyProcessResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(airline,"airline");
		RequestCheckUtils.checkMaxLength(airline,2,"airline");
		RequestCheckUtils.checkNotEmpty(arrAirports,"arrAirports");
		RequestCheckUtils.checkMaxLength(arrAirports,100,"arrAirports");
		RequestCheckUtils.checkMaxLength(attributes,300,"attributes");
		RequestCheckUtils.checkNotEmpty(cabinRules,"cabinRules");
		RequestCheckUtils.checkMaxLength(cabinRules,300,"cabinRules");
		RequestCheckUtils.checkMaxLength(changeRule,300,"changeRule");
		RequestCheckUtils.checkNotEmpty(dayOfWeeks,"dayOfWeeks");
		RequestCheckUtils.checkMaxLength(dayOfWeeks,7,"dayOfWeeks");
		RequestCheckUtils.checkNotEmpty(depAirports,"depAirports");
		RequestCheckUtils.checkMaxLength(depAirports,100,"depAirports");
		RequestCheckUtils.checkMaxLength(ei,20,"ei");
		RequestCheckUtils.checkMaxLength(excludeDate,200,"excludeDate");
		RequestCheckUtils.checkMaxValue(flags,9223372036854775807L,"flags");
		RequestCheckUtils.checkMinValue(flags,0L,"flags");
		RequestCheckUtils.checkMaxLength(flightInfo,1000,"flightInfo");
		RequestCheckUtils.checkMaxLength(memo,500,"memo");
		RequestCheckUtils.checkMaxLength(officeId,32,"officeId");
		RequestCheckUtils.checkMaxLength(outProductId,64,"outProductId");
		RequestCheckUtils.checkNotEmpty(policyType,"policyType");
		RequestCheckUtils.checkMaxLength(refundRule,300,"refundRule");
		RequestCheckUtils.checkMaxLength(reissueRule,300,"reissueRule");
		RequestCheckUtils.checkNotEmpty(saleEndDate,"saleEndDate");
		RequestCheckUtils.checkNotEmpty(saleStartDate,"saleStartDate");
		RequestCheckUtils.checkMaxLength(seatInfo,1500,"seatInfo");
		RequestCheckUtils.checkMaxLength(specialRule,300,"specialRule");
		RequestCheckUtils.checkNotEmpty(travelEndDate,"travelEndDate");
		RequestCheckUtils.checkNotEmpty(travelStartDate,"travelStartDate");
		RequestCheckUtils.checkNotEmpty(type,"type");
		RequestCheckUtils.checkMaxValue(type,2L,"type");
		RequestCheckUtils.checkMinValue(type,0L,"type");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
