package com.peacebird.dataserver.bean.comp;

import java.util.Comparator;

public class ChannelComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		int diff = getIndex(o1) - getIndex(o2);
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	private int getIndex(String channel) {
		if (channel == null) {
			return 0;
		}
		if (channel.equals("自营")||channel.equals("直营")) {
			return 1;
		} else if (channel.equals("加盟")) {
			return 2;
		} else if (channel.equals("联营")) {
			return 3;
		} else if (channel.equals("分销")) {
			return 4;
		} else if (channel.equals("魔法")) {
			return 5;
		} else if (channel.equals("其他")) {
			return 6;
		} else {
			return 6;
		}
	}

}
