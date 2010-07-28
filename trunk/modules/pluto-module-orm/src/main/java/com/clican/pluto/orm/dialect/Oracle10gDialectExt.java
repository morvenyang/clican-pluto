/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wezhang
 *
 */
package com.clican.pluto.orm.dialect;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.mapping.Column;

public class Oracle10gDialectExt extends Oracle10gDialect implements
		DialectExtention {

	public String getModifyColumnString(Column column) {
		return "modify column";
	}

	public String getRenameColumnString(String oldName, String newName) {
		return "rename column " + oldName + " to " + newName;
	}

	public boolean isAddColumnDefinitionWhenRename() {
		return false;
	}

	public boolean needDropForeignKeyBeforeDropColumn() {
		return false;
	}

	public String getDropFKString() {
		return "drop constraint";
	}

}

// $Id$