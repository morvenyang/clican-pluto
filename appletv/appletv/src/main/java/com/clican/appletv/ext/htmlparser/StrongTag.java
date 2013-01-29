package com.clican.appletv.ext.htmlparser;

import org.htmlparser.tags.CompositeTag;

public class StrongTag extends CompositeTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6751992167771745501L;
	
	private static final String[] mIds = new String[] { "strong" };

	public String[] getIds() {
		return mIds;
	}

	public String[] getEnders() {
		return mIds;
	}
}
