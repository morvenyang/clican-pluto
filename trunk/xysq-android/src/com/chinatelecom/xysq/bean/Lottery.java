package com.chinatelecom.xysq.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Lottery implements Parcelable{

	private int lottery;
	
	private int money;
	
	private boolean share;

	public int getLottery() {
		return lottery;
	}

	public void setLottery(int lottery) {
		this.lottery = lottery;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public boolean isShare() {
		return share;
	}

	public void setShare(boolean share) {
		this.share = share;
	}

	public static final Parcelable.Creator<Lottery> CREATOR = new Parcelable.Creator<Lottery>() {
		public Lottery createFromParcel(Parcel in) {
			return new Lottery(in);
		}

		public Lottery[] newArray(int size) {
			return new Lottery[size];
		}
	};

	public Lottery(){
		
	}
	private Lottery(Parcel in) {
		lottery = in.readInt();
		money = in.readInt();
		share = in.readInt()==1?true:false;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(lottery);
		dest.writeInt(money);
		dest.writeInt(share?1:0);
	}
	
	
}
