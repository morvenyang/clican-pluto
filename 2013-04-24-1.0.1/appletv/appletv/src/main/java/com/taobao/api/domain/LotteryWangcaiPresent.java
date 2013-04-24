package com.taobao.api.domain;

import java.util.Date;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 旺彩赠送DO
 *
 * @author auto create
 * @since 1.0, null
 */
public class LotteryWangcaiPresent extends TaobaoObject {

	private static final long serialVersionUID = 3764793319133492374L;

	/**
	 * 彩种名称
	 */
	@ApiField("lottery_name")
	private String lotteryName;

	/**
	 * 赠送时间
	 */
	@ApiField("present_date")
	private Date presentDate;

	/**
	 * 彩票注数
	 */
	@ApiField("stake_count")
	private Long stakeCount;

	/**
	 * 用户昵称
	 */
	@ApiField("user_nick")
	private String userNick;

	/**
	 * 中奖金额，以分为单位
	 */
	@ApiField("win_fee")
	private Long winFee;

	public String getLotteryName() {
		return this.lotteryName;
	}
	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public Date getPresentDate() {
		return this.presentDate;
	}
	public void setPresentDate(Date presentDate) {
		this.presentDate = presentDate;
	}

	public Long getStakeCount() {
		return this.stakeCount;
	}
	public void setStakeCount(Long stakeCount) {
		this.stakeCount = stakeCount;
	}

	public String getUserNick() {
		return this.userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public Long getWinFee() {
		return this.winFee;
	}
	public void setWinFee(Long winFee) {
		this.winFee = winFee;
	}

}
