package com.chinatelecom.xysq.enumeration;

public enum YyyResult {

	SHARED(0),
	
	NOT_SHARED(1),
	
	SHAKE(2);
	
	private int value;
	
	private YyyResult(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
