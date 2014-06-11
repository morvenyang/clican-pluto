package com.peacebird.dataserver.bean;

public class RetailResult implements Comparable<RetailResult> {

	private String type;// channel, sort, region

	private String name;

	private Integer dayAmount;

	public RetailResult(String type, String name, Number dayAmount) {
		this.type = type;
		this.name = name;
		if (dayAmount != null) {
			this.dayAmount = dayAmount.intValue();
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(Integer dayAmount) {
		this.dayAmount = dayAmount;
	}

	private int getIndex() {
		if (type.equals("channel")) {
			if (name == null) {
				return 0;
			}
			if (name.equals("自营") || name.equals("直营")) {
				return 1;
			} else if (name.equals("加盟")) {
				return 2;
			} else if (name.equals("联营")) {
				return 3;
			} else if (name.equals("分销")) {
				return 4;
			} else if (name.equals("魔法")) {
				return 5;
			} else if (name.equals("其他")) {
				return 6;
			} else {
				return 6;
			}
		} else if (type.equals("sort")) {
			if (name == null) {
				return 0;
			}
			if (name.equals("街店")) {
				return 1;
			} else if (name.equals("购物中心")) {
				return 2;
			} else if (name.equals("百货")) {
				return 3;
			} else if (name.equals("卖场店铺")) {
				return 4;
			} else if (name.equals("其他")) {
				return 5;
			} else {
				return 5;
			}
		} else {
			return 0;
		}

	}

	@Override
	public int compareTo(RetailResult o) {
		int diff = this.getIndex() - o.getIndex();
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}

}
