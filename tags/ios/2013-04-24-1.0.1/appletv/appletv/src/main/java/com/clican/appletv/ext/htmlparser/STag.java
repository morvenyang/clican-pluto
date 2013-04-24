package com.clican.appletv.ext.htmlparser;

import org.htmlparser.tags.CompositeTag;

public class STag extends CompositeTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4169042538006006863L;
	
	private static final String[] mIds = new String[] { "s" };

	public String[] getIds() {
		return mIds;
	}

	public String[] getEnders() {
		return mIds;
	}
}
