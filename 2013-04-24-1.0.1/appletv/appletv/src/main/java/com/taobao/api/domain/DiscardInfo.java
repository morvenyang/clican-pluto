package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 用户丢失消息的数据结构
 *
 * @author auto create
 * @since 1.0, null
 */
public class DiscardInfo extends TaobaoObject {

	private static final long serialVersionUID = 4678967454465768759L;

	/**
	 * 丢弃消息的结束时间
	 */
	@ApiField("end")
	private Long end;

	/**
	 * 用户nick
	 */
	@ApiField("nick")
	private String nick;

	/**
	 * 丢弃消息的开始时间
	 */
	@ApiField("start")
	private Long start;

	/**
	 * 非授权消息订阅的关键字，比如按商品编号订阅时，此值为num_iid
	 */
	@ApiField("subscribe_key")
	private String subscribeKey;

	/**
	 * 非授权消息订阅的值，比如按商品编号订阅时，此值为商品的具体编号
	 */
	@ApiField("subscribe_value")
	private String subscribeValue;

	/**
	 * 消息类型
	 */
	@ApiField("type")
	private String type;

	/**
	 * 用户id
	 */
	@ApiField("user_id")
	private Long userId;

	public Long getEnd() {
		return this.end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}

	public String getNick() {
		return this.nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	public Long getStart() {
		return this.start;
	}
	public void setStart(Long start) {
		this.start = start;
	}

	public String getSubscribeKey() {
		return this.subscribeKey;
	}
	public void setSubscribeKey(String subscribeKey) {
		this.subscribeKey = subscribeKey;
	}

	public String getSubscribeValue() {
		return this.subscribeValue;
	}
	public void setSubscribeValue(String subscribeValue) {
		this.subscribeValue = subscribeValue;
	}

	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
