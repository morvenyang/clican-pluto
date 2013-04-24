/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.common.type;

public class BlobType extends CommonType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888523072698364776L;

	@Override
	public Class<?> getClazz() {
		return java.sql.Blob.class;
	}

	@Override
	public String getDeclareString() {
		return "byte[]";
	}

	

}


//$Id$