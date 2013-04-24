/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dialect;

import org.hibernate.dialect.HSQLDialect;
import org.hibernate.mapping.Column;

public class HSQLDialectExt extends HSQLDialect implements DialectExtention {

	public String getModifyColumnString(Column column) {
		return "alter column";
	}

	public String getRenameColumnString(String oldName, String newName) {
		return "alter column "+oldName+" rename to "+newName;
	}

	public boolean isAddColumnDefinitionWhenRename() {
		return false;
	}

	public boolean needDropForeignKeyBeforeDropColumn() {
		return true;
	}

	public String getDropFKString() {
		return "drop constraint";
	}

}


//$Id$