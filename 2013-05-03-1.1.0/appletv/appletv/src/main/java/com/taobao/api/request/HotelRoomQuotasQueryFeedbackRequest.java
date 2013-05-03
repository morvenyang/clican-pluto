package com.taobao.api.request;

import java.util.Date;
import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.HotelRoomQuotasQueryFeedbackResponse;
import com.taobao.api.ApiRuleException;
/**
 * TOP API: taobao.hotel.room.quotas.query.feedback request
 * 
 * @author auto create
 * @since 1.0, 2013-01-24 12:41:47
 */
public class HotelRoomQuotasQueryFeedbackRequest implements TaobaoRequest<HotelRoomQuotasQueryFeedbackResponse> {

	private TaobaoHashMap udfParams; // add user-defined text parameters
	private Long timestamp;

	/** 
	* 选中日期范围内的最大可用房量
	 */
	private Long avaliableRoomCount;

	/** 
	* 入住酒店的日期
	 */
	private Date checkinDate;

	/** 
	* 离开酒店的日期
	 */
	private Date checkoutDate;

	/** 
	* 失败原因,当result为F时,此项为必填,最长200个字符
	 */
	private String failedReason;

	/** 
	* 指令消息中的messageid,最长128字符
	 */
	private String messageId;

	/** 
	* 预订结果 
S:成功
F:失败
	 */
	private String result;

	/** 
	* 从入住时期到离店日期的每日一间房价与预定房量,JSON格式传递。 date：代表房态日期，格式为YYYY-MM-DD， price：代表当天房价，0～99999900，存储的单位是分，货币单位为人民币，num：代表当天剩余房量，0～999，所有日期的预订间数应该一致。 如： [{"date":2011-01-28,"price":10000, "num":10},{"date":2011-01-29,"price":12000,"num":10}],最长1500个字符
	 */
	private String roomQuotas;

	/** 
	* 订单总价。0～99999999的正整数。货币单位为人民币。
	 */
	private Long totalRoomPrice;

	public void setAvaliableRoomCount(Long avaliableRoomCount) {
		this.avaliableRoomCount = avaliableRoomCount;
	}
	public Long getAvaliableRoomCount() {
		return this.avaliableRoomCount;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}
	public Date getCheckinDate() {
		return this.checkinDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	public Date getCheckoutDate() {
		return this.checkoutDate;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	public String getFailedReason() {
		return this.failedReason;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageId() {
		return this.messageId;
	}

	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return this.result;
	}

	public void setRoomQuotas(String roomQuotas) {
		this.roomQuotas = roomQuotas;
	}
	public String getRoomQuotas() {
		return this.roomQuotas;
	}

	public void setTotalRoomPrice(Long totalRoomPrice) {
		this.totalRoomPrice = totalRoomPrice;
	}
	public Long getTotalRoomPrice() {
		return this.totalRoomPrice;
	}
	private Map<String,String> headerMap=new TaobaoHashMap();
	
	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiMethodName() {
		return "taobao.hotel.room.quotas.query.feedback";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("avaliable_room_count", this.avaliableRoomCount);
		txtParams.put("checkin_date", this.checkinDate);
		txtParams.put("checkout_date", this.checkoutDate);
		txtParams.put("failed_reason", this.failedReason);
		txtParams.put("message_id", this.messageId);
		txtParams.put("result", this.result);
		txtParams.put("room_quotas", this.roomQuotas);
		txtParams.put("total_room_price", this.totalRoomPrice);
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

	public Class<HotelRoomQuotasQueryFeedbackResponse> getResponseClass() {
		return HotelRoomQuotasQueryFeedbackResponse.class;
	}

	public void check() throws ApiRuleException {
		
		RequestCheckUtils.checkNotEmpty(avaliableRoomCount,"avaliableRoomCount");
		RequestCheckUtils.checkMinValue(avaliableRoomCount,0L,"avaliableRoomCount");
		RequestCheckUtils.checkNotEmpty(checkinDate,"checkinDate");
		RequestCheckUtils.checkNotEmpty(checkoutDate,"checkoutDate");
		RequestCheckUtils.checkNotEmpty(messageId,"messageId");
		RequestCheckUtils.checkNotEmpty(result,"result");
		RequestCheckUtils.checkNotEmpty(totalRoomPrice,"totalRoomPrice");
		RequestCheckUtils.checkMaxValue(totalRoomPrice,99999999L,"totalRoomPrice");
		RequestCheckUtils.checkMinValue(totalRoomPrice,0L,"totalRoomPrice");
	}
	
	public Map<String,String> getHeaderMap() {
		return headerMap;
	}
}
