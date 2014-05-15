package com.peacebird.dataserver.bean;

import java.util.List;
import java.util.Map;

public class RankStatResult {

	private Map<String,List<RankResult>> channel;
	
	private int result;
	private String message;
	
	public Map<String, List<RankResult>> getChannel() {
		return channel;
	}
	public void setChannel(Map<String, List<RankResult>> channel) {
		this.channel = channel;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
