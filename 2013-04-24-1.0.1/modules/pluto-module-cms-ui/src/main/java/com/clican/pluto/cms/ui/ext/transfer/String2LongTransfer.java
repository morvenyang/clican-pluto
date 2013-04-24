package com.clican.pluto.cms.ui.ext.transfer;

import org.apache.commons.collections.Transformer;

public class String2LongTransfer implements Transformer {

	public Object transform(Object input) {
		return Long.parseLong(input.toString().split(",")[0]);
	}

}
