package com.taobao.api.internal.jushita.stream;

import java.util.ArrayList;
import java.util.List;

import com.taobao.api.internal.util.json.JSONWriter;

/**
 * Created by IntelliJ IDEA.
 * User: guichen - anson
 * Date: 12-8-13
 */
public class Report {
	private List<String> sOffset = new ArrayList<String>();
	private List<String> fOffset = new ArrayList<String>();

	public List<String> getsOffset() {
		return sOffset;
	}

	public void setsOffset(List<String> sOffset) {
		this.sOffset = sOffset;
	}

	public List<String> getfOffset() {
		return fOffset;
	}

	public void setfOffset(List<String> fOffset) {
		this.fOffset = fOffset;
	}

	public String asJson() {
		JSONWriter jsonWriter = new JSONWriter();
		return jsonWriter.write(this);
	}
}
