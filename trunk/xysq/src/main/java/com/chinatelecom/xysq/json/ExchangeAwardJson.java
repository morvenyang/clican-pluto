package com.chinatelecom.xysq.json;

import java.util.List;

import com.chinatelecom.xysq.model.Store;

public class ExchangeAwardJson {

	private boolean success;
	
	private String message;
	
	private String name;
	
	private String code;
	
	private Long id;
	
	private boolean received;
	
	private List<StoreJson> stores;
	
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

	public List<StoreJson> getStores() {
		return stores;
	}

	public void setStores(List<StoreJson> stores) {
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

	
	
	
}
