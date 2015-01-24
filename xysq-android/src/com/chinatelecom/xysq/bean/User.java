package com.chinatelecom.xysq.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

	private String nickName;
	
	private String msisdn;
	
	private String jsessionid;

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
		nickName=in.readString();
		msisdn = in.readString();
		jsessionid = in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nickName);
		dest.writeString(msisdn);
		dest.writeString(jsessionid);
	}
	
}
