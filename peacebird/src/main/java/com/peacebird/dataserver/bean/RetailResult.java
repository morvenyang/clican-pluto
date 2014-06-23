package com.peacebird.dataserver.bean;

import java.util.HashMap;
import java.util.Map;

public class RetailResult implements Comparable<RetailResult> {

	private String type;// channel, sort, region

	private String name;

	private Long dayAmount;

	private Double percent;
	

	public RetailResult(String type, String name, Number dayAmount) {
		this.type = type;
		this.name = name;
		if (dayAmount != null) {
			this.dayAmount = dayAmount.longValue();
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

	public Long getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(Long dayAmount) {
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

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public Map<String, Object> getLabel() {
		Map<String,Object> label = new HashMap<String,Object>();
		label.put("align", "center");
		return label;
	}



	public String getLineBreakLabel(){
		if(this.name==null){
			return this.name;
		}
		String result = "";
		int index = 0;
		while(this.name.length()>index+4){
			result += this.name.substring(index,index+4)+"\n";
			index+=4;
		}
		result+=this.name.substring(index);
		return result;
	}
	public String getDescription() {
		if (percent != null) {
			String da = dayAmount + "";
			String p = String.format("%.1f", this.percent * 100) + "%";
			while (p.length() < 7) {
				p = " " + p;
			}
			String description = da + "万元" + p;
			return description;
		} else {
			return "";
		}
	}

}
