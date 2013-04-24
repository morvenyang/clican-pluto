package com.taobao.api.domain;

import java.util.Date;
import java.util.List;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 开通增量消息服务的应用用户
 *
 * @author auto create
 * @since 1.0, null
 */
public class AppCustomer extends TaobaoObject {

	private static final long serialVersionUID = 4565344637336187784L;

	/**
	 * isv为用户开通增量消息服务的时间
	 */
	@ApiField("created")
	private Date created;

	/**
	 * 最后修改时间，开通、用户的session生效或失效都会更改这个时间。
	 */
	@ApiField("modified")
	private Date modified;

	/**
	 * 开通用户的淘宝昵称
	 */
	@ApiField("nick")
	private String nick;

	/**
	 * 应用用户开通增量消息服务的状态：有两个返回值valid_session和invalid_session。valid_session表示已开通且seesion有效；invalid_session已开通但session失效，此时，无法接收该用户的消息。
	 */
	@ApiField("status")
	private String status;

	/**
	 * 应用为用户开通的消息类型。只有用户开通的消息在应用所订阅的消息类别集合内时，应用才能收到相应的消息。例如：应用订阅添加商品，用户开通了添加商品和删除商品，此时应用只能收到添加商品的消息，收不到删除商品的消息。
	 */
	@ApiListField("subscriptions")
	@ApiField("subscription")
	private List<Subscription> subscriptions;

	/**
	 * 用户使用的功能。get表示增量api取消息功能；notify表示主动通知功能；syn表示同步数据到ISV数据库功能。
	 */
	@ApiListField("type")
	@ApiField("string")
	private List<String> type;

	/**
	 * 用户编号
	 */
	@ApiField("user_id")
	private Long userId;

	public Date getCreated() {
		return this.created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return this.modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getNick() {
		return this.nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<Subscription> getSubscriptions() {
		return this.subscriptions;
	}
	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public List<String> getType() {
		return this.type;
	}
	public void setType(List<String> type) {
		this.type = type;
	}

	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
