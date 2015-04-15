package com.chinatelecom.xysq.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ExchangeAward implements Parcelable{

	private boolean success;
	
	private String message;
	
	private String name;
	
	private String code;
	
	private Long id;
	
	private boolean received;
	
	private List<Store> stores;
	
	private boolean realGood;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	public boolean isRealGood() {
		return realGood;
	}

	public void setRealGood(boolean realGood) {
		this.realGood = realGood;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public static final Parcelable.Creator<ExchangeAward> CREATOR = new Parcelable.Creator<ExchangeAward>() {
		public ExchangeAward createFromParcel(Parcel in) {
			return new ExchangeAward(in);
		}

		public ExchangeAward[] newArray(int size) {
			return new ExchangeAward[size];
		}
	};

	public ExchangeAward(){
		
	}
	private ExchangeAward(Parcel in) {
		success = in.readInt()==0?false:true;
		code = in.readString();
		name = in.readString();
		id = in.readLong();
		received = in.readInt()==0?false:true;
		realGood = in.readInt()==0?false:true;
		stores = new ArrayList<Store>();
		in.readTypedList(stores, Store.CREATOR);
	}

	
	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if(success){
			dest.writeInt(1);
		}else{
			dest.writeInt(0);
		}
		dest.writeString(code);
		dest.writeString(name);
		dest.writeLong(id);
		if(received){
			dest.writeInt(1);
		}else{
			dest.writeInt(0);
		}
		if(realGood){
			dest.writeInt(1);
		}else{
			dest.writeInt(0);
		}
		dest.writeTypedList(this.stores);
	}
	
}
