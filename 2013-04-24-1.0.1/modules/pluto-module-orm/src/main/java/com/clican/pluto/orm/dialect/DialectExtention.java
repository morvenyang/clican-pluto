/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dialect;

import org.hibernate.mapping.Column;

public interface DialectExtention {

	public String getModifyColumnString(Column column);

	public String getRenameColumnString(String oldName, String newName);

	public boolean isAddColumnDefinitionWhenRename();

	public boolean needDropForeignKeyBeforeDropColumn();
	
	public String getDropFKString();

}

// $Id$