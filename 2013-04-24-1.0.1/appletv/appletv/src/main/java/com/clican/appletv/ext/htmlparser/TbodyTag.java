package com.clican.appletv.ext.htmlparser;

import org.htmlparser.tags.CompositeTag;

public class TbodyTag extends CompositeTag{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2704464192698771399L;
	private static final String[] mIds = new String[] { "tbody" };

	public String[] getIds() {
		return mIds;
	}

	public String[] getEnders() {
		return mIds;
	}
}
