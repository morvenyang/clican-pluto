package com.clican.appletv.ext.htmlparser;

import org.htmlparser.tags.CompositeTag;

public class EmTag extends CompositeTag {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4018055479771279550L;
	private static final String[] mIds = new String[] { "em" };

	public String[] getIds() {
		return mIds;
	}

	public String[] getEnders() {
		return mIds;
	}
}
