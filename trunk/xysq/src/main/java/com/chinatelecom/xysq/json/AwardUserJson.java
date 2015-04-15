package com.chinatelecom.xysq.json;

import java.util.List;

public class AwardUserJson {

	private int money;
	
	private List<AwardJson> awards;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public List<AwardJson> getAwards() {
		return awards;
	}

	public void setAwards(List<AwardJson> awards) {
		this.awards = awards;
	}
	
	
}
