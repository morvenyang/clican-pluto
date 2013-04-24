package com.taobao.api.domain;

import java.util.Date;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 机票产品政策数据对象
 *
 * @author auto create
 * @since 1.0, null
 */
public class Policy extends TaobaoObject {

	private static final long serialVersionUID = 5471471523294262564L;

	/**
	 * 当前政策的代理商id，必需参数
	 */
	@ApiField("agent_id")
	private Long agentId;

	/**
	 * 当前政策支持的航空公司二字码，必需参数
	 */
	@ApiField("airline")
	private String airline;

	/**
	 * 当前政策支持的到达机场三字码列表，逗号分隔字符串，必需参数
	 */
	@ApiField("arr_airports")
	private String arrAirports;

	/**
	 * 当前政策的属性信息，扩展使用
	 */
	@ApiField("attributes")
	private String attributes;

	/**
	 * 当前政策是否自动HK，默认为非自动HK
	 */
	@ApiField("auto_hk_flag")
	private Boolean autoHkFlag;

	/**
	 * 当前政策的是否自动出票，默认为非自动出票
	 */
	@ApiField("auto_ticket_flag")
	private Boolean autoTicketFlag;

	/**
	 * @1: [{"matcher":{"mode":"ALL","list":["D"]},"priceStrategy":{"mode":
"TICKET_PRICE","modeBaseValue":500,"retention":500,"rebase":-5}}]
@2: [{"matcher"
:{"mode":"ALL","list":["D"]},"priceStrategy":{"mode":"DISCOUNT"
,"modeBaseValue"
:null,"retention":300,"rebase":-5}}]
@3: [{"matcher":{"mode":"ALL","list":["D"]},"priceStrategy":{"mode":"Y_DISCOUNT","modeBaseValue":75,"retention":500,"rebase":-5}}]
	 */
	@ApiField("cabin_rules")
	private String cabinRules;

	/**
	 * 当前政策支持的出发机场三字码列表，逗号分隔字符串，必需参数
	 */
	@ApiField("dep_airports")
	private String depAirports;

	/**
	 * 当前政策销售日期最少提前天数
	 */
	@ApiField("first_sale_advance_day")
	private Long firstSaleAdvanceDay;

	/**
	 * 设置包含/排除的航班号，注意格式： +CA1234,CZ3169表示包含列表， -CA1234,CZ3169表示排除列表默认是全部
	 */
	@ApiField("flight_info")
	private String flightInfo;

	/**
	 * 政策淘宝机票的主键id
	 */
	@ApiField("id")
	private Long id;

	/**
	 * 当前政策销售日期最晚 提前天数
	 */
	@ApiField("last_sale_advance_day")
	private Long lastSaleAdvanceDay;

	/**
	 * 当前政策的外部产品id，用于关联代理商自身维护的政策id，必需参数，使用接口调用时唯一标识政策
	 */
	@ApiField("out_product_id")
	private String outProductId;

	/**
	 * 当前政策的详细信息，必需
	 */
	@ApiField("policy_detail")
	private PolicyDetail policyDetail;

	/**
	 * 当前政策的政策类型：6，特价政策；8，让利政策；10，特殊政策，必需参数
	 */
	@ApiField("policy_type")
	private Long policyType;

	/**
	 * 当前政策销售有效日期的截止日期(注意，格式如：20120-03-10 10:30:31 等价2012-03-11)必需参数
	 */
	@ApiField("sale_end_date")
	private Date saleEndDate;

	/**
	 * 当前政策销售有效日期的开始日期(注意，格式如：20120-03-10 10:30:31 等价2012-03-11)必需参数
	 */
	@ApiField("sale_start_date")
	private Date saleStartDate;

	/**
	 * 当前特殊政策的库存信息，特殊政策时必需，待补充
	 */
	@ApiField("seat_info")
	private String seatInfo;

	/**
	 * 当前政策是否支持共享航班，默认为不支持共享航班(不支持共享航班时，如果当前航班为非承运航班，则搜索结果也不展现)
	 */
	@ApiField("share_support")
	private Boolean shareSupport;

	/**
	 * 当前政策的状态: 0，关闭；1，发布；2，删除；3，无效(淘宝机票自动程序处理的中间状态)
必需参数，注意如果当前为发布状态，发布后即刻可以从前台搜索到，从而可能即刻产生用订单
	 */
	@ApiField("status")
	private Long status;

	/**
	 * 当前政策旅行有效日期的结束日期(注意，格式如：20120-03-10 10:30:31 等价2012-03-11)必需参数
	 */
	@ApiField("travel_end_date")
	private Date travelEndDate;

	/**
	 * 当前政策旅行有效日期的开始日期(注意，格式，如：2012-03-10 10:30:31 等价2012-03-10)必需参数
	 */
	@ApiField("travel_start_date")
	private Date travelStartDate;

	/**
	 * 行程类型: 0，单程政策；1，往返政策，必需参数
	 */
	@ApiField("trip_type")
	private Long tripType;

	public Long getAgentId() {
		return this.agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getAirline() {
		return this.airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}

	public String getArrAirports() {
		return this.arrAirports;
	}
	public void setArrAirports(String arrAirports) {
		this.arrAirports = arrAirports;
	}

	public String getAttributes() {
		return this.attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public Boolean getAutoHkFlag() {
		return this.autoHkFlag;
	}
	public void setAutoHkFlag(Boolean autoHkFlag) {
		this.autoHkFlag = autoHkFlag;
	}

	public Boolean getAutoTicketFlag() {
		return this.autoTicketFlag;
	}
	public void setAutoTicketFlag(Boolean autoTicketFlag) {
		this.autoTicketFlag = autoTicketFlag;
	}

	public String getCabinRules() {
		return this.cabinRules;
	}
	public void setCabinRules(String cabinRules) {
		this.cabinRules = cabinRules;
	}

	public String getDepAirports() {
		return this.depAirports;
	}
	public void setDepAirports(String depAirports) {
		this.depAirports = depAirports;
	}

	public Long getFirstSaleAdvanceDay() {
		return this.firstSaleAdvanceDay;
	}
	public void setFirstSaleAdvanceDay(Long firstSaleAdvanceDay) {
		this.firstSaleAdvanceDay = firstSaleAdvanceDay;
	}

	public String getFlightInfo() {
		return this.flightInfo;
	}
	public void setFlightInfo(String flightInfo) {
		this.flightInfo = flightInfo;
	}

	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getLastSaleAdvanceDay() {
		return this.lastSaleAdvanceDay;
	}
	public void setLastSaleAdvanceDay(Long lastSaleAdvanceDay) {
		this.lastSaleAdvanceDay = lastSaleAdvanceDay;
	}

	public String getOutProductId() {
		return this.outProductId;
	}
	public void setOutProductId(String outProductId) {
		this.outProductId = outProductId;
	}

	public PolicyDetail getPolicyDetail() {
		return this.policyDetail;
	}
	public void setPolicyDetail(PolicyDetail policyDetail) {
		this.policyDetail = policyDetail;
	}

	public Long getPolicyType() {
		return this.policyType;
	}
	public void setPolicyType(Long policyType) {
		this.policyType = policyType;
	}

	public Date getSaleEndDate() {
		return this.saleEndDate;
	}
	public void setSaleEndDate(Date saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public Date getSaleStartDate() {
		return this.saleStartDate;
	}
	public void setSaleStartDate(Date saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public String getSeatInfo() {
		return this.seatInfo;
	}
	public void setSeatInfo(String seatInfo) {
		this.seatInfo = seatInfo;
	}

	public Boolean getShareSupport() {
		return this.shareSupport;
	}
	public void setShareSupport(Boolean shareSupport) {
		this.shareSupport = shareSupport;
	}

	public Long getStatus() {
		return this.status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getTravelEndDate() {
		return this.travelEndDate;
	}
	public void setTravelEndDate(Date travelEndDate) {
		this.travelEndDate = travelEndDate;
	}

	public Date getTravelStartDate() {
		return this.travelStartDate;
	}
	public void setTravelStartDate(Date travelStartDate) {
		this.travelStartDate = travelStartDate;
	}

	public Long getTripType() {
		return this.tripType;
	}
	public void setTripType(Long tripType) {
		this.tripType = tripType;
	}

}
