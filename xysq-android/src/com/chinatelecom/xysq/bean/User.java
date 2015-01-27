package com.chinatelecom.xysq.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

	private Long id;
	private String nickName;
	
	private String msisdn;
	
	private String jsessionid;
	
	private String address;
	private String carNumber;
	private boolean applyXqnc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getJsessionid() {
		return jsessionid;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public boolean isApplyXqnc() {
		return applyXqnc;
	}

	public void setApplyXqnc(boolean applyXqnc) {
		this.applyXqnc = applyXqnc;
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	public User(){
		
	}
	private User(Parcel in) {
		id = in.readLong();
		nickName=in.readString();
		msisdn = in.readString();
		jsessionid = in.readString();
		address = in.readString();
		carNumber = in.readString();
		applyXqnc = in.readInt()==1?true:false;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(nickName);
		dest.writeString(msisdn);
		dest.writeString(jsessionid);
		dest.writeString(address);
		dest.writeString(carNumber);
		dest.writeInt(applyXqnc?1:0);
	}
	
}
