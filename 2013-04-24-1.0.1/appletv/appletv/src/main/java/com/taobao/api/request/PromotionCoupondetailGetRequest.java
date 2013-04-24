package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.PromotionCoupondetailGetResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.promotion.coupondetail.get request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class PromotionCoupondetailGetRequest implements TaobaoRequest<PromotionCoupondetailGetResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 买家昵称
	 */
	private String buyerNick;

	/** 
	* 优惠券的id
	 */
	private Long couponId;

	/** 
	* 优惠券截至时间 如果截至日期不输入的话，查询当前日期向前15天的数据，否则，查询输入的截至日期向前15天的数据。
	 */
	private Date endTime;

	/** 
	* 查询的页号，结果集是分页返回的，每页20条
	 */
	private Long pageNo;

	/** 
	* 每页行数
	 */
	private Long pageSize;

	/** 
	* 优惠券使用情况unused：代表未使用using：代表使用中used：代表已使用。必须是unused，using，used
	 */
	private String state;

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public String getBuyerNick() {
		return this.buyerNick;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public Long getCouponId() {
		return this.couponId;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getEndTime() {
		return this.endTime;
	}

	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}
	public Long getPageNo() {
		return this.pageNo;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageSize() {
		return this.pageSize;
	}

	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return this.state;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.promotion.coupondetail.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("buyer_nick", this.buyerNick);
		txtParams.put("coupon_id", this.couponId);
		txtParams.put("end_time", this.endTime);
		txtParams.put("page_no", this.pageNo);
		txtParams.put("page_size", this.pageSize);
		txtParams.put("state", this.state);
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

	public Class<PromotionCoupondetailGetResponse> getResponseClass() {
		return PromotionCoupondetailGetResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(couponId,"couponId");
		RequestCheckUtils.checkMaxValue(pageSize,20L,"pageSize");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
